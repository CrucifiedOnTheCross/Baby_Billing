package com.nexign.brt.exception;

public class TariffNotFoundException extends RuntimeException {
    public TariffNotFoundException(Integer id) {
        super("Tariff with ID " + id + " not found.");
    }
}