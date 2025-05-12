package com.nexign.hrs.controller;

import com.nexign.hrs.exception.TariffArchivedException;
import com.nexign.hrs.exception.TariffNotFoundException;
import com.nexign.hrs.service.TariffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tariff")
public class TariffInfoController {

    private final TariffService tariffService;

    @GetMapping("/{tariffId}")
    public ResponseEntity<?> getTariffParameters(@PathVariable Integer tariffId) {
        try {
            Map<String, Object> parameters = tariffService.getTariffParameters(tariffId);
            return ResponseEntity.ok(Map.of("tariff", parameters));
        } catch (TariffNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Tariff not found"));
        } catch (TariffArchivedException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Tariff is archived"));
        }
    }

}

