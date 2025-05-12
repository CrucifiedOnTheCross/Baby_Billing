package com.nexign.crm.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequestDto {

    private String msisdn;

    private BigDecimal money;

}
