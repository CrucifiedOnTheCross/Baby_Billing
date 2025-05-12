package com.nexign.crm.client;

import com.nexign.crm.dto.*;
import com.nexign.crm.exception.BrtClientException;
import com.nexign.crm.exception.SubscriberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class BrtClient {

    private final RestTemplate restTemplate;

    @Value("${brt.clients-base-url}")
    private String clientsBaseUrl;

    public ClientDto registerUser(CreateUserRequest request, BigDecimal initialBalance, LocalDateTime registrationDate) {
        String url = clientsBaseUrl + "/register";
        ClientDto clientDto = ClientDto.builder()
                .firstName(request.getFirstName())
                .middleName(request.getMiddleName())
                .lastName(request.getLastName())
                .msisdn(request.getMsisdn())
                .tariffId(request.getTariffId())
                .money(initialBalance)
                .date(registrationDate)
                .build();

        try {
            HttpEntity<ClientDto> entity = new HttpEntity<>(clientDto);
            ResponseEntity<ClientDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    ClientDto.class
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            return null;
        }
    }

    public SubscriberInfoResponseDto getSubscriberInfo(String phoneNumber) {
        String url = "http://localhost:8081/v1/clients/" + phoneNumber + "/info";
        try {
            return restTemplate.getForObject(url, SubscriberInfoResponseDto.class);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new SubscriberNotFoundException("Абонент с номером " + phoneNumber + " не найден");
        } catch (HttpClientErrorException ex) {
            throw new BrtClientException("Ошибка при обращении к BRT: " + ex.getStatusCode(), ex);
        }
    }

    public boolean changeTariff(String msisdn, Integer tariffId) {
        String url = String.format("%s/%s/changeTariff", clientsBaseUrl, msisdn);
        TariffChangeRequestDto requestDto = new TariffChangeRequestDto();
        requestDto.setTariffId(tariffId);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    new HttpEntity<>(requestDto),
                    String.class
            );
            return response.getStatusCode().is2xxSuccessful();
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to change tariff for client: " + msisdn, e);
        }
    }

    public PaymentResponseDto payBalance(String msisdn, PaymentRequestDto paymentRequest) {
        String url = String.format("%s/%s/pay", clientsBaseUrl, msisdn);

        try {
            HttpEntity<PaymentRequestDto> entity = new HttpEntity<>(paymentRequest);
            ResponseEntity<PaymentResponseDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    entity,
                    PaymentResponseDto.class
            );
            return response.getBody();
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new BrtClientException("Ошибка при пополнении баланса для клиента: " + msisdn, e);
        }
    }

}