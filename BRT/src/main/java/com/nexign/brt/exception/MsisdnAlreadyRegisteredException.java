package com.nexign.brt.exception;

public class MsisdnAlreadyRegisteredException extends RuntimeException {
    public MsisdnAlreadyRegisteredException(String msisdn) {
        super("MSISDN already registered: " + msisdn);
    }
}