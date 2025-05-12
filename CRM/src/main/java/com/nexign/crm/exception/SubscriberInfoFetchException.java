package com.nexign.crm.exception;

public class SubscriberInfoFetchException extends RuntimeException {
    public SubscriberInfoFetchException(String msisdn, Throwable cause) {
        super("Failed to get information about the subscriber with the number " + msisdn, cause);
    }
}
