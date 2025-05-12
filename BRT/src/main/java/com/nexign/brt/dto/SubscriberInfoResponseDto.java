package com.nexign.brt.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class SubscriberInfoResponseDto {

    private String msisdn;

    private String lastName;

    private String firstName;

    private String middleName;

    private String date;

    private BigDecimal money;

    private long tariffId;

    private String tariffName;

    private String tariffParameters;

}
