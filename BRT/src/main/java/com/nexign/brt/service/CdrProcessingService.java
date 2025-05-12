package com.nexign.brt.service;

import com.nexign.brt.dto.CdrDto;
import com.nexign.brt.entity.CallEntity;
import com.nexign.brt.entity.ClientEntity;
import com.nexign.brt.repository.CallRepository;
import com.nexign.brt.repository.ClientRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CdrProcessingService {

    private final CallRepository callRepository;
    private final ClientRepository clientRepository;
    private final Validator validator;

    public void process(List<CdrDto> calls) {
        log.info("Processing {} CDR records", calls.size());

        List<CallEntity> validCalls = calls.stream()
                .filter(this::isValid)
                .filter(this::ownerExists)
                .map(this::mapToEntity)
                .collect(Collectors.toList());

        if (!validCalls.isEmpty()) {
            callRepository.saveAll(validCalls);
            log.info("Saved {} valid CDR records", validCalls.size());
        } else {
            log.warn("No valid CDR records to save");
        }
    }

    private boolean isValid(CdrDto dto) {
        Set<ConstraintViolation<CdrDto>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            log.warn("Validation failed: {} for DTO: {}", violations, dto);
            return false;
        }
        return true;
    }

    private boolean ownerExists(CdrDto dto) {
        Optional<ClientEntity> clientOpt = clientRepository.findClientByMsisdn(dto.getClientMsisdn());
        if (clientOpt.isEmpty()) {
            log.warn("Client with MSISDN {} not found in DB. Skipping this record.", dto.getClientMsisdn());
            return false;
        }
        return true;
    }

    private CallEntity mapToEntity(CdrDto dto) {
        Optional<ClientEntity> clientOpt = clientRepository.findClientByMsisdn(dto.getClientMsisdn());

        if (clientOpt.isEmpty()) {
            log.warn("Client with MSISDN {} not found. Skipping this record.", dto.getClientMsisdn());
            return null;
        }

        ClientEntity client = clientOpt.get();

        return CallEntity.builder()
                .type(dto.getType())
                .msisdnClient(dto.getClientMsisdn())
                .msisdnPartner(dto.getPartnerMsisdn())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .tariffId(client.getTariff().getId())
                .isPartnerClient(true)
                .statusId((short) 1)
                .build();
    }

}
