package com.nexign.hrs.exception;

public class TariffNotFoundException extends RuntimeException {
    public TariffNotFoundException(String tariffNotFound) {
        super("Tariff not found");
    }
}
