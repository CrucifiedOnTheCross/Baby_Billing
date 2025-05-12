package com.nexign.cdr.service;

import com.nexign.cdr.entity.Call;
import com.nexign.cdr.entity.Subscriber;
import com.nexign.cdr.repository.CallRepository;
import com.nexign.cdr.repository.SubscriberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CallGenerationService {

    private final SubscriberRepository subscriberRepository;
    private final CallRepository callRepository;
    private final ExecutorService executor;
    private final CsvExportService csvExportService;
    @Value("${call-simulator.batch-size}")
    private int batchSize;

    public void generateAndSaveCalls(int totalCount) throws InterruptedException {
        List<Subscriber> subscribers = subscriberRepository.findAll();
        List<Future<List<Call>>> futures = new ArrayList<>();

        for (int i = 0; i < totalCount; i++) {
            futures.add(executor.submit(() -> generateCall(subscribers)));
        }

        List<Call> allCalls = new ArrayList<>();
        for (Future<List<Call>> future : futures) {
            try {
                allCalls.addAll(future.get());
            } catch (ExecutionException e) {
                log.error("Error occurred while retrieving generated calls from future", e);
            }
        }

        List<Call> filtered = filterOverlaps(allCalls);
        List<Call> mirrored = createMirroredCalls(filtered);

        csvExportService.exportCalls(mirrored);
        saveCallsInBatches(mirrored);
    }

    private List<Call> generateCall(List<Subscriber> subscribers) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        Subscriber client = subscribers.get(random.nextInt(subscribers.size()));
        Subscriber partner;

        do {
            partner = subscribers.get(random.nextInt(subscribers.size()));
        } while (partner.getMsisdn().equals(client.getMsisdn()));

        LocalDateTime start = randomDateTimeInYear(random);
        int duration = 60 + random.nextInt(3600);
        LocalDateTime end = start.plusSeconds(duration);
        String callType = random.nextBoolean() ? "01" : "02";

        Call first = Call.builder()
                .type(callType)
                .clientMsisdn(client.getMsisdn())
                .partnerMsisdn(partner.getMsisdn())
                .startTime(start)
                .endTime(end.toLocalDate().isAfter(start.toLocalDate()) ? start.toLocalDate().atTime(23, 59, 59) : end)
                .build();

        if (end.toLocalDate().isAfter(start.toLocalDate())) {
            Call second = Call.builder()
                    .type(callType)
                    .clientMsisdn(client.getMsisdn())
                    .partnerMsisdn(partner.getMsisdn())
                    .startTime(end.toLocalDate().atStartOfDay())
                    .endTime(end)
                    .build();
            return List.of(first, second);
        }

        return List.of(first);
    }

    private LocalDateTime randomDateTimeInYear(ThreadLocalRandom random) {
        LocalDate start = LocalDate.now().minusYears(1);
        LocalDate end = LocalDate.now();
        long days = ChronoUnit.DAYS.between(start, end);

        LocalDate randomDate = start.plusDays(random.nextInt((int) days));
        int hour = random.nextInt(24);
        int minute = random.nextInt(60);
        int second = random.nextInt(60);

        return LocalDateTime.of(randomDate, LocalTime.of(hour, minute, second));
    }

    private List<Call> filterOverlaps(List<Call> calls) {
        Set<Call> validCalls = new HashSet<>();

        Map<String, List<Call>> byClient = calls.stream()
                .collect(Collectors.groupingBy(Call::getClientMsisdn));

        collectNonOverlappingCalls(validCalls, byClient);

        Map<String, List<Call>> byPartner = validCalls.stream()
                .collect(Collectors.groupingBy(Call::getPartnerMsisdn));

        Set<Call> finalValidCalls = new HashSet<>();

        collectNonOverlappingCalls(finalValidCalls, byPartner);

        return new ArrayList<>(finalValidCalls);
    }

    private void collectNonOverlappingCalls(Set<Call> validCalls, Map<String, List<Call>> byClient) {
        for (Map.Entry<String, List<Call>> entry : byClient.entrySet()) {
            List<Call> sorted = entry.getValue().stream()
                    .sorted(Comparator.comparing(Call::getStartTime))
                    .toList();

            LocalDateTime lastEnd = null;
            for (Call call : sorted) {
                if (lastEnd == null || call.getStartTime().isAfter(lastEnd)) {
                    validCalls.add(call);
                    lastEnd = call.getEndTime();
                }
            }
        }
    }

    private void saveCallsInBatches(List<Call> calls) {
        for (int i = 0; i < calls.size(); i += batchSize) {
            int end = Math.min(i + batchSize, calls.size());
            List<Call> batch = calls.subList(i, end);
            callRepository.saveAll(batch);
        }
    }


    private List<Call> createMirroredCalls(List<Call> calls) {
        List<Call> result = new ArrayList<>(calls.size() * 2);
        for (Call call : calls) {
            result.add(call);
            result.add(createMirroredCall(call));
        }
        return result;
    }


    private Call createMirroredCall(Call original) {
        return Call.builder()
                .type(invertCallType(original.getType()))
                .clientMsisdn(original.getPartnerMsisdn())
                .partnerMsisdn(original.getClientMsisdn())
                .startTime(original.getStartTime())
                .endTime(original.getEndTime())
                .build();
    }

    private String invertCallType(String type) {
        return "01".equals(type) ? "02" : "01";
    }

}