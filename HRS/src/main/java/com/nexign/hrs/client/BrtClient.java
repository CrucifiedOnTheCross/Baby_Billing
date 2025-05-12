package com.nexign.hrs.client;

import com.nexign.hrs.dto.TariffDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class BrtClient {

    private final RestTemplate restTemplate;

    @Value("${brt.api.url}")
    private String baseUrl;

    public void chargeMonthlyFee(String msisdn, BigDecimal amount) {
        String url = baseUrl + "/api/payments/charge";
        Map<String, Object> payload = Map.of(
                "msisdn", msisdn,
                "amount", amount
        );

        restTemplate.postForEntity(url, payload, Void.class);
    }

    public TariffDto getCurrentTariff(String msisdn) {
        String url = baseUrl + "/api/tariff/current?msisdn=" + msisdn;
        return restTemplate.getForObject(url, TariffDto.class);
    }
}
