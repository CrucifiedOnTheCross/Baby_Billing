package com.nexign.brt.exception;

public class TariffNotActiveException extends RuntimeException {
    public TariffNotActiveException(Integer id) {
        super("Tariff with ID " + id + " is archived and cannot be assigned.");
    }
}