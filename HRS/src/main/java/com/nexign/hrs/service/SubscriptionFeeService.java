package com.nexign.hrs.service;

import com.nexign.hrs.client.BrtClient;
import com.nexign.hrs.entity.MonthTariffHistoryEntity;
import com.nexign.hrs.entity.TariffEntity;
import com.nexign.hrs.repository.MonthTariffHistoryRepository;
import com.nexign.hrs.repository.TariffRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionFeeService {

    private final MonthTariffHistoryRepository monthTariffHistoryRepository;
    private final TariffRepository tariffRepository;
    private final BrtClient brtClient;

    @Scheduled(cron = "0 0 0 * * *")
    public void chargeMonthlyFees() {
        LocalDate today = LocalDate.now();

        List<MonthTariffHistoryEntity> expiringPeriods =
                monthTariffHistoryRepository.findByPeriodEnd(today.atStartOfDay());

        if (expiringPeriods.isEmpty()) {
            return;
        }

        for (MonthTariffHistoryEntity period : expiringPeriods) {
            TariffEntity tariff = period.getTariff();

            if (tariff.getType() != 2) {
                continue;
            }

            try {
                BigDecimal amount = new BigDecimal(
                        String.valueOf(tariff.getParameters().getOrDefault("monthlyFee", "0"))
                );

                brtClient.chargeMonthlyFee(period.getMsisdn(), amount);

            } catch (Exception e) {
                log.error("Error when subscriber subscription fee is debited from the subscriber {}: {}", period.getMsisdn(), e.getMessage());
            }
        }
    }
}
