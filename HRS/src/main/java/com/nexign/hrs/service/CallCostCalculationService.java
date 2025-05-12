package com.nexign.hrs.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexign.hrs.dto.CallRequestDto;
import com.nexign.hrs.dto.CallResponseDto;
import com.nexign.hrs.entity.MonthTariffHistoryEntity;
import com.nexign.hrs.entity.TariffEntity;
import com.nexign.hrs.repository.MonthTariffHistoryRepository;
import com.nexign.hrs.repository.TariffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CallCostCalculationService {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final TariffRepository tariffRepository;
    private final MonthTariffHistoryRepository MonthTariffHistoryEntityRepository;
    private final MonthTariffHistoryRepository monthTariffHistoryRepository;

    public CallResponseDto calculateCallCost(CallRequestDto callRequestDto) {
        Optional<TariffEntity> tariffOpt = tariffRepository.findById(callRequestDto.getTariffId());
        if (tariffOpt.isEmpty()) {
            throw new IllegalArgumentException("Tariff not found");
        }

        TariffEntity tariff = tariffOpt.get();
        BigDecimal callDuration = BigDecimal.valueOf(callRequestDto.getCallDurationMinutes()).setScale(0, RoundingMode.UP);

        if (tariff.getType() == 1) {
            BigDecimal cost = calculateClassicTariffCost(callDuration, tariff);
            return new CallResponseDto(cost);
        } else if (tariff.getType() == 2) {
            return calculateMonthlyTariffCost(callRequestDto, tariff, callDuration);
        } else {
            throw new IllegalArgumentException("Invalid tariff type");
        }
    }

    private BigDecimal calculateClassicTariffCost(BigDecimal callDuration, TariffEntity tariff) {
        try {
            JsonNode rootNode = objectMapper.valueToTree(tariff.getParameters());

            JsonNode costNode = rootNode.path("outgoingCalls").path("external").path("costPerMinute");
            if (costNode.isMissingNode()) {
                throw new IllegalArgumentException("costPerMinute not found in tariff parameters");
            }

            BigDecimal costPerMinute = BigDecimal.valueOf(costNode.asDouble());
            return callDuration.multiply(costPerMinute);

        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to calculate call cost", e);
        }
    }

    private CallResponseDto calculateMonthlyTariffCost(CallRequestDto callRequestDto, TariffEntity tariff, BigDecimal callDuration) {
        Optional<MonthTariffHistoryEntity> activePeriodOpt = MonthTariffHistoryEntityRepository
                .findActivePeriod(callRequestDto.getMsisdn());

        if (activePeriodOpt.isEmpty()) {
            MonthTariffHistoryEntity newPeriod = createNewMonthTariffPeriod(
                    callRequestDto.getMsisdn(),
                    tariff,
                    LocalDateTime.now() // Начинаем период с текущего времени
            );
            activePeriodOpt = Optional.of(newPeriod);
            MonthTariffHistoryEntityRepository.save(newPeriod);
        }

        MonthTariffHistoryEntity activePeriod = activePeriodOpt.get();
        int remainingMinutes = activePeriod.getMinuteBalance();
        BigDecimal excessMinutes = callDuration.subtract(BigDecimal.valueOf(remainingMinutes));

        if (excessMinutes.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal excessCost = calculateExcessCost(excessMinutes);
            return new CallResponseDto(excessCost);
        } else {
            activePeriod.setMinuteBalance(remainingMinutes - callDuration.intValue());
            MonthTariffHistoryEntityRepository.save(activePeriod);
            return new CallResponseDto(BigDecimal.ZERO);
        }
    }

    private MonthTariffHistoryEntity createNewMonthTariffPeriod(String msisdn, TariffEntity tariff, LocalDateTime startDate) {
        MonthTariffHistoryEntity newPeriod = new MonthTariffHistoryEntity();
        newPeriod.setMsisdn(msisdn);
        newPeriod.setTariff(tariff);


        Integer includedMinutes = (Integer) tariff.getParameters().get("includedMinutes");
        if (includedMinutes == null) {
            throw new IllegalArgumentException("Tariff parameters must contain includedMinutes");
        }
        newPeriod.setMinuteBalance(includedMinutes);

        newPeriod.setPeriodStart(startDate);
        newPeriod.setPeriodEnd(startDate.plusMonths(1)); // Период длится 1 месяц

        return newPeriod;
    }

    private LocalDateTime getPreviousTariffPeriodEndDate(String msisdn) {
        Optional<MonthTariffHistoryEntity> previousPeriodOpt = monthTariffHistoryRepository.findLastPeriodByMsisdn(msisdn);
        return previousPeriodOpt.map(MonthTariffHistoryEntity::getPeriodEnd).orElse(null);
    }

    private BigDecimal calculateExcessCost(BigDecimal excessMinutes) {
        try {
            TariffEntity tariff = tariffRepository.findById(11)
                    .orElseThrow(() -> new IllegalArgumentException("Tariff with ID 11 not found"));

            Map<String, Object> params = tariff.getParameters();
            Map<String, Object> outgoingCalls = (Map<String, Object>) params.get("outgoingCalls");
            Map<String, Object> externalCalls = (Map<String, Object>) outgoingCalls.get("external");
            Double costPerMinute = (Double) externalCalls.get("costPerMinute");

            if (costPerMinute == null) {
                throw new IllegalArgumentException("Cost per minute not specified in tariff");
            }

            return excessMinutes.multiply(BigDecimal.valueOf(costPerMinute));
        } catch (ClassCastException | NullPointerException e) {
            throw new IllegalArgumentException("Invalid tariff parameters format", e);
        }
    }

}
