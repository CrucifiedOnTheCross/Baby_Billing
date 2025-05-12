package com.nexign.hrs.service;

import com.nexign.hrs.dto.TariffChangeRequest;
import com.nexign.hrs.entity.MonthTariffHistoryEntity;
import com.nexign.hrs.entity.TariffEntity;
import com.nexign.hrs.exception.TariffNotFoundException;
import com.nexign.hrs.repository.MonthTariffHistoryRepository;
import com.nexign.hrs.repository.TariffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TariffChangeService {

    private final TariffRepository tariffRepository;
    private final MonthTariffHistoryRepository monthTariffHistoryRepository;

    public void processTariffChange(TariffChangeRequest request) {
        TariffEntity tariff = tariffRepository.findById(request.getTariffId())
                .orElseThrow(() -> new TariffNotFoundException("Tariff not found"));

        if (!isMonthlyTariff(tariff)) {
            return;
        }

        MonthTariffHistoryEntity period = new MonthTariffHistoryEntity();
        period.setMsisdn(request.getMsisdn());
        period.setTariff(tariff);

        int minuteBalance = (Integer) tariff.getParameters().getOrDefault("includedMinutes", 0);
        period.setMinuteBalance(minuteBalance);

        LocalDate start = request.getChangeDate();
        LocalDate end = start.plusDays(30);

        period.setPeriodStart(start.atStartOfDay());
        period.setPeriodEnd(end.atStartOfDay());

        monthTariffHistoryRepository.save(period);
    }

    private boolean isMonthlyTariff(TariffEntity tariff) {
        return tariff.getType() == 2;
    }

}
