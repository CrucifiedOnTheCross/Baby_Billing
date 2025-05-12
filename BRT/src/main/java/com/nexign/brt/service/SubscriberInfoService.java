package com.nexign.brt.service;

import com.nexign.brt.dto.SubscriberInfoResponseDto;
import com.nexign.brt.entity.ClientEntity;
import com.nexign.brt.entity.TariffEntity;
import com.nexign.brt.exception.DataProcessingException;
import com.nexign.brt.exception.InvalidTariffIdException;
import com.nexign.brt.exception.SubscriberNotFoundException;
import com.nexign.brt.repository.ClientRepository;
import com.nexign.brt.repository.TariffRepository;
import org.springframework.stereotype.Service;

@Service
public class SubscriberInfoService {

    private final ClientRepository clientRepository;
    private final TariffRepository tariffRepository;
    private final TarifficationClientService hrsService;

    public SubscriberInfoService(ClientRepository clientRepository,
                                 TariffRepository tariffRepository,
                                 TarifficationClientService tarifficationClientService) {
        this.clientRepository = clientRepository;
        this.tariffRepository = tariffRepository;
        this.hrsService = tarifficationClientService;
    }

    public SubscriberInfoResponseDto getSubscriberInfo(String msisdn) {
        ClientEntity client = clientRepository.findClientByMsisdn(msisdn)
                .orElseThrow(() -> new SubscriberNotFoundException("Subscriber not found"));

        TariffEntity tariff = tariffRepository.findById(client.getTariff().getId())
                .orElseThrow(() -> new InvalidTariffIdException("Invalid or unavailable tariffId"));

        String tariffParameters = hrsService.getTariffParameters(tariff.getId());

        try {
            return SubscriberInfoResponseDto.builder()
                    .msisdn(client.getMsisdn())
                    .lastName(client.getLastName())
                    .firstName(client.getFirstName())
                    .middleName(client.getMiddleName())
                    .date(client.getDate().toString())
                    .money(client.getMoney())
                    .tariffId(tariff.getId())
                    .tariffParameters(tariffParameters)
                    .build();
        } catch (Exception e) {
            throw new DataProcessingException("Failed to process subscriber data");
        }
    }
}

