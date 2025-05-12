package com.nexign.crm.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class CreateClientResponse {

    private String lastName;

    private String firstName;

    private String middleName;

    private String msisdn;

    private Integer tariffId;

    private BigDecimal money;

    private LocalDateTime date;

}
