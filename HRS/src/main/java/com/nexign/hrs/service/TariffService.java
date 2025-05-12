package com.nexign.hrs.service;

import com.nexign.hrs.entity.TariffEntity;
import com.nexign.hrs.exception.TariffArchivedException;
import com.nexign.hrs.exception.TariffNotFoundException;
import com.nexign.hrs.repository.TariffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TariffService {

    private final TariffRepository tariffRepository;

    public Map<String, Object> getTariffParameters(Integer tariffId) {
        TariffEntity tariff = tariffRepository.findById(tariffId)
                .orElseThrow(() -> new TariffNotFoundException("Tariff not found"));

        if (tariff.getIsArchived()) {
            throw new TariffArchivedException("Tariff is archived");
        }

        return tariff.getParameters();
    }

}