package com.nexign.hrs.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CallRequestDto {
    private String msisdn;
    private int tariffId;
    private double callDurationMinutes;
}
