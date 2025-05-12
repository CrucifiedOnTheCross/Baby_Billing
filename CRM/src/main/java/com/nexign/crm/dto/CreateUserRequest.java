package com.nexign.crm.dto;

import lombok.Data;

@Data
public class CreateUserRequest {

    private String lastName;

    private String firstName;

    private String middleName;

    private String msisdn;

    private Integer tariffId;

}
