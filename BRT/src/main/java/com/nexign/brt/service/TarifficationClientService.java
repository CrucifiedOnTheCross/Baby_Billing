package com.nexign.brt.service;

import com.nexign.brt.dto.CallRequestDto;
import com.nexign.brt.dto.CallResponseDto;
import com.nexign.brt.exception.HrsServiceUnavailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class TarifficationClientService {

    private final RestTemplate restTemplate;

    @Value("${hrs.uri.calculate}")
    private String calculateUri;

    @Value("${hrs.uri.params}")
    private String paramsUri;

    public CallResponseDto calculateCost(CallRequestDto request) {
        return restTemplate.postForObject(calculateUri, request, CallResponseDto.class);
    }

    public String getTariffParameters(Integer tariffId) {
        String url = String.format("%s/%d", paramsUri, tariffId);
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new HrsServiceUnavailableException("HRS service returned non-200 status");
            }
        } catch (Exception e) {
            throw new HrsServiceUnavailableException("Failed to retrieve tariff parameters from HRS");
        }
    }

}
