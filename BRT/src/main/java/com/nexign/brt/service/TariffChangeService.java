package com.nexign.brt.service;

import com.nexign.brt.entity.ClientEntity;
import com.nexign.brt.entity.TariffEntity;
import com.nexign.brt.exception.ClientNotFoundException;
import com.nexign.brt.exception.TariffNotActiveException;
import com.nexign.brt.exception.TariffNotFoundException;
import com.nexign.brt.repository.ClientRepository;
import com.nexign.brt.repository.TariffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TariffChangeService {

    private final ClientRepository clientRepository;
    private final TariffRepository tariffRepository;

    public boolean changeTariff(String msisdn, Integer tariffId) {
        ClientEntity client = clientRepository.findClientByMsisdn(msisdn)
                .orElseThrow(() -> new ClientNotFoundException(msisdn));

        TariffEntity tariff = tariffRepository.findById(tariffId)
                .orElseThrow(() -> new TariffNotFoundException(tariffId));

        if (tariff.getIsArchived()) {
            throw new TariffNotActiveException(tariffId);
        }

        client.setTariff(tariff);
        clientRepository.save(client);
        return true;
    }
}
