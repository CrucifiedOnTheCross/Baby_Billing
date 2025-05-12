package com.nexign.brt.service;

import com.nexign.brt.dto.ClientDto;
import com.nexign.brt.entity.ClientEntity;
import com.nexign.brt.entity.TariffEntity;
import com.nexign.brt.exception.InvalidTariffException;
import com.nexign.brt.exception.MsisdnAlreadyRegisteredException;
import com.nexign.brt.repository.ClientRepository;
import com.nexign.brt.repository.TariffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientsRegistrationService {

    private final ClientRepository clientRepository;
    private final TariffRepository tariffRepository;

    public boolean registerSubscriber(ClientDto requestDTO) {
        if (clientRepository.findClientByMsisdn(requestDTO.getMsisdn()).isPresent()) {
            throw new MsisdnAlreadyRegisteredException(requestDTO.getMsisdn());
        }

        TariffEntity tariff = tariffRepository.findById(requestDTO.getTariffId())
                .orElseThrow(() -> new InvalidTariffException(requestDTO.getTariffId()));

        if (tariff.getIsArchived()) {
            throw new InvalidTariffException(requestDTO.getTariffId());
        }

        ClientEntity newClient = ClientEntity.builder()
                .lastName(requestDTO.getLastName())
                .firstName(requestDTO.getFirstName())
                .middleName(requestDTO.getMiddleName())
                .msisdn(requestDTO.getMsisdn())
                .tariff(tariff)
                .money(requestDTO.getMoney())
                .date(requestDTO.getDate())
                .build();

        clientRepository.save(newClient);
        return true;
    }
}
