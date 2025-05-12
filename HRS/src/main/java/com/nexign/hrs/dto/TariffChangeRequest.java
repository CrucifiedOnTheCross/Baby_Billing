package com.nexign.hrs.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TariffChangeRequest {
    private String msisdn;
    private Integer tariffId;
    private LocalDate changeDate;
}
