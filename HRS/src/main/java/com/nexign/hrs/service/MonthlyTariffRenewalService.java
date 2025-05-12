package com.nexign.hrs.service;

import com.nexign.hrs.client.BrtClient;
import com.nexign.hrs.dto.TariffDto;
import com.nexign.hrs.entity.MonthTariffHistoryEntity;
import com.nexign.hrs.entity.TariffEntity;
import com.nexign.hrs.repository.MonthTariffHistoryRepository;
import com.nexign.hrs.repository.TariffRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MonthlyTariffRenewalService {

    private final MonthTariffHistoryRepository monthTariffHistoryRepository;
    private final BrtClient brtClient;
    private final TariffRepository tariffRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void renewMonthlyTariffs() {
        LocalDate yesterday = LocalDate.now().minusDays(1);

        List<MonthTariffHistoryEntity> expiringPeriods =
                monthTariffHistoryRepository.findAllByPeriodEnd(yesterday.atStartOfDay());

        for (MonthTariffHistoryEntity oldPeriod : expiringPeriods) {
            String msisdn = oldPeriod.getMsisdn();

            try {
                TariffDto currentTariff = brtClient.getCurrentTariff(msisdn);

                if (currentTariff.getType() == 2) {
                    TariffEntity tariffEntity = tariffRepository.findById(currentTariff.getId())
                            .orElseThrow(() -> new IllegalStateException("Tariff not found in local DB"));

                    MonthTariffHistoryEntity newPeriod = new MonthTariffHistoryEntity();
                    newPeriod.setMsisdn(msisdn);
                    newPeriod.setTariff(tariffEntity);
                    newPeriod.setMinuteBalance((Integer) tariffEntity.getParameters().getOrDefault("includedMinutes", 0));

                    LocalDate start = oldPeriod.getPeriodEnd().toLocalDate();
                    LocalDate end = start.plusDays(30);

                    newPeriod.setPeriodStart(start.atStartOfDay());
                    newPeriod.setPeriodEnd(end.atStartOfDay());

                    monthTariffHistoryRepository.save(newPeriod);
                }
            } catch (HttpServerErrorException.ServiceUnavailable e) {
                log.warn("BRT unavailable for msisdn {}. Will retry later.", msisdn);
            } catch (Exception e) {
                log.error("Error renewing tariff for msisdn {}: {}", msisdn, e.getMessage());
            }
        }
    }
}
