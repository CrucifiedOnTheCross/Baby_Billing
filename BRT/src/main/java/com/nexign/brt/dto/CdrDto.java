package com.nexign.brt.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CdrDto {

    @Pattern(regexp = "01|02", message = "Type must be either '01' or '02'")
    String type;

    @Pattern(regexp = "^7\\d{10}$", message = "Client must be 11 digits starting with 7")
    String clientMsisdn;

    @Pattern(regexp = "^7\\d{10}$", message = "Partner must be 11 digits starting with 7")
    String partnerMsisdn;

    @NotNull
    LocalDateTime startTime;

    @NotNull
    LocalDateTime endTime;

    @AssertTrue(message = "Start date must be before end date")
    private boolean isStartDateBeforeEndDate() {
        return startTime != null && endTime != null && startTime.isBefore(endTime);
    }

    @AssertTrue(message = "Client equals partner")
    private boolean isInitiatorEqualsReceiver() {
        return clientMsisdn != null && partnerMsisdn != null && !clientMsisdn.equals(partnerMsisdn);
    }

}
