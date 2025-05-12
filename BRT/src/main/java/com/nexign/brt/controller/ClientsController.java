package com.nexign.brt.controller;

import com.nexign.brt.dto.*;
import com.nexign.brt.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/clients")
@RequiredArgsConstructor
public class ClientsController {

    private final SubscriberInfoService subscriberInfoService;
    private final ClientsRegistrationService subscriberRegistrationService;
    private final PaymentService paymentService;
    private final TariffChangeService tariffChangeService;
    private final CdrProcessingService cdrProcessingService;

    @GetMapping("/{msisdn}/info")
    public ResponseEntity<SubscriberInfoResponseDto> getSubscriberInfo(@PathVariable String msisdn) {
        SubscriberInfoResponseDto subscriberInfo = subscriberInfoService.getSubscriberInfo(msisdn);
        return ResponseEntity.ok(subscriberInfo);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerSubscriber(@RequestBody ClientDto requestDTO) {
        boolean success = subscriberRegistrationService.registerSubscriber(requestDTO);
        if (success) {
            return ResponseEntity.ok(requestDTO);
        } else {
            return ResponseEntity.status(400).body("Failed to register subscriber.");
        }
    }

    @PutMapping("/{msisdn}/pay")
    public ResponseEntity<?> payBalance(
            @PathVariable String msisdn,
            @RequestBody PaymentRequestDto paymentRequest) {
        try {
            PaymentResponseDto response = paymentService.payBalance(msisdn, paymentRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to update balance due to internal error");
        }
    }

    @PutMapping("/{msisdn}/changeTariff")
    public ResponseEntity<String> changeTariff(@PathVariable String msisdn,
                                               @RequestBody TariffChangeRequestDto requestDTO) {
        boolean success = tariffChangeService.changeTariff(msisdn, requestDTO.getTariffId());

        if (success) {
            return ResponseEntity.ok("Tariff updated successfully.");
        } else {
            return ResponseEntity.status(400).body("Failed to update tariff.");
        }
    }
}