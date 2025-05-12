package com.nexign.crm.controllor;

import com.nexign.crm.client.BrtClient;
import com.nexign.crm.dto.*;
import com.nexign.crm.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/clients")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final BrtClient brtClient;

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }

    @GetMapping("/{msisdn}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> getUser(@PathVariable String msisdn) {
        SubscriberInfoResponseDto subscriberInfo = brtClient.getSubscriberInfo(msisdn);
        return ResponseEntity.ok(subscriberInfo);
    }

    @PutMapping("/{msisdn}/tariff")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> changeTariff(@PathVariable String msisdn,
                                          @RequestBody TariffChangeRequestDto request) {
        boolean success = brtClient.changeTariff(msisdn, request.getTariffId());

        if (success) {
            return ResponseEntity.ok("The tariff has been successfully changed.");
        } else {
            return ResponseEntity.status(400).body("Failed to change the tariff.");
        }
    }

    @PostMapping("/{msisdn}/balance")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER')")
    public ResponseEntity<?> payBalance(
            @PathVariable String msisdn,
            @RequestBody PaymentRequestDto paymentRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserMsisdn = authentication.getName();

        if ("USER".equals(authentication.getAuthorities().toArray()[0].toString()) && !currentUserMsisdn.equals(msisdn)) {
            return ResponseEntity.status(403).body("You can only top up your own balance.");
        }

        try {
            PaymentResponseDto response = brtClient.payBalance(msisdn, paymentRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to update balance due to internal error");
        }
    }
}

