package com.nexign.hrs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class CallResponseDto {
    private BigDecimal cost;
}
