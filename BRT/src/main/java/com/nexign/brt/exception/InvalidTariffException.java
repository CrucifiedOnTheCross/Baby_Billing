package com.nexign.brt.exception;

public class InvalidTariffException extends RuntimeException {
    public InvalidTariffException(Integer tariffId) {
        super("Invalid or unavailable tariffId: " + tariffId);
    }
}