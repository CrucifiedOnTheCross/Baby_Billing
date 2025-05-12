package com.nexign.brt.exception;

public class InvalidPaymentAmountException extends IllegalArgumentException {
    public InvalidPaymentAmountException(String message) {
        super(message);
    }
}
