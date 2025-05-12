package com.nexign.hrs.controller;

import com.nexign.hrs.dto.CallRequestDto;
import com.nexign.hrs.dto.CallResponseDto;
import com.nexign.hrs.service.CallCostCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/call")
public class CallController {

    private final CallCostCalculationService callCostCalculationService;

    @PostMapping("/calculate-cost")
    public ResponseEntity<CallResponseDto> calculateCallCost(@RequestBody CallRequestDto callRequestDto) {
        CallResponseDto response = callCostCalculationService.calculateCallCost(callRequestDto);
        return ResponseEntity.ok(response);
    }

}
