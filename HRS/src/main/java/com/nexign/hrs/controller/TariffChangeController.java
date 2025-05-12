package com.nexign.hrs.controller;

import com.nexign.hrs.dto.TariffChangeRequest;
import com.nexign.hrs.exception.TariffNotFoundException;
import com.nexign.hrs.service.TariffChangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tariff")
public class TariffChangeController {

    private final TariffChangeService tariffChangeService;

    @PostMapping("/change")
    public ResponseEntity<?> handleTariffChange(@RequestBody TariffChangeRequest request) {
        try {
            tariffChangeService.processTariffChange(request);
            return ResponseEntity.ok().build();
        } catch (TariffNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Tariff not found"));
        }
    }
}

