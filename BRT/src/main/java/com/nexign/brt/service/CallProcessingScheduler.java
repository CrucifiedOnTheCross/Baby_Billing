package com.nexign.brt.service;

import com.nexign.brt.dto.CallRequestDto;
import com.nexign.brt.dto.CallResponseDto;
import com.nexign.brt.dto.CdrDto;
import com.nexign.brt.entity.CallEntity;
import com.nexign.brt.entity.ClientEntity;
import com.nexign.brt.entity.TransactionEntity;
import com.nexign.brt.repository.CallRepository;
import com.nexign.brt.repository.ClientRepository;
import com.nexign.brt.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class CallProcessingScheduler {

    private final CallRepository callRepository;
    private final ClientRepository clientRepository;
    private final TransactionRepository transactionRepository;
    private final TarifficationClientService tarifficationClientService;

    @Scheduled(fixedDelay = 5000)
    public void processPendingCalls() {
        log.info("Starting call processing task...");
        while (processCalls()) ;
    }

    @Transactional
    public boolean processCalls() {
        List<CallEntity> calls;
        calls = callRepository.findCallEntitiesByStatusId((short) 1);

        if (calls.isEmpty())
            return false;

        for (CallEntity call : calls) {
            try {
                log.info("Processing call ID: {}", call.getId());

                long callDurationInSeconds = Duration.between(call.getStartTime(), call.getEndTime()).getSeconds();
                double callDurationInMinutes = Math.ceil(callDurationInSeconds / 60.0);

                CallRequestDto requestDto = CallRequestDto.builder()
                        .msisdn(call.getMsisdnClient())
                        .tariffId(call.getTariffId())
                        .callDurationMinutes(callDurationInMinutes)
                        .build();

                CallResponseDto response = tarifficationClientService.calculateCost(requestDto);

                if (response != null) {
                    updateClientBalance(call.getMsisdnClient(), response.getCost());
                    updateCallStatus(call);
                    saveTransaction(call, response.getCost());

                    log.info("Successfully processed call ID: {}. Cost: {}", call.getId(), response.getCost());
                } else {
                    log.warn("Cost calculation failed for call ID: {}", call.getId());
                }
            } catch (Exception e) {
                log.error("Failed to process call ID: {} due to error: {}", call.getId(), e.getMessage(), e);
            }
        }
        return true;
    }


    private CdrDto mapToCdrDTO(CallEntity call) {
        return new CdrDto(
                call.getType(),
                call.getMsisdnClient(),
                call.getMsisdnPartner(),
                call.getStartTime(),
                call.getEndTime()
        );
    }

    private void updateClientBalance(String msisdn, BigDecimal cost) {
        ClientEntity client = clientRepository.findClientByMsisdn(msisdn)
                .orElseThrow(() -> {
                    log.error("Client not found: {}", msisdn);
                    return new RuntimeException("Client not found");
                });

        BigDecimal newBalance = client.getMoney().subtract(cost);
        client.setMoney(newBalance);
        clientRepository.save(client);

        log.debug("Updated balance for client {}: -{} => new balance: {}", msisdn, cost, newBalance);
    }

    private void updateCallStatus(CallEntity call) {
        call.setStatusId((short) 3);
        callRepository.save(call);
        log.debug("Call status updated to PROCESSED for call ID: {}", call.getId());
    }

    private void saveTransaction(CallEntity call, BigDecimal cost) {
        ClientEntity client = clientRepository.findClientByMsisdn(call.getMsisdnClient())
                .orElseThrow(() -> new RuntimeException("Client not found"));

        TransactionEntity transaction = TransactionEntity.builder()
                .msisdn(client)
                .money(cost)
                .description("DEBIT for call ID: " + call.getId())
                .date(LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);
        log.debug("Transaction saved for call ID: {} | Amount: {}", call.getId(), cost);
    }

}