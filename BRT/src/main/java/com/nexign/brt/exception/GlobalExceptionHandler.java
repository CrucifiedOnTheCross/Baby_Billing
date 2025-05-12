package com.nexign.brt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<String> handleClientNotFound(ClientNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(SubscriberNotFoundException.class)
    public ResponseEntity<String> handleSubscriberNotFound(SubscriberNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(DataProcessingException.class)
    public ResponseEntity<String> handleDataProcessing(DataProcessingException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to process subscriber data.");
    }

    @ExceptionHandler(HrsServiceUnavailableException.class)
    public ResponseEntity<String> handleHrsUnavailable(HrsServiceUnavailableException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("HRS service is currently unavailable.");
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<String> handleInternalError(InternalServerErrorException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An internal error occurred.");
    }

    @ExceptionHandler(InvalidPaymentAmountException.class)
    public ResponseEntity<String> handleInvalidPayment(InvalidPaymentAmountException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidTariffIdException.class)
    public ResponseEntity<String> handleInvalidTariff(InvalidTariffIdException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or unavailable tariff ID.");
    }

    @ExceptionHandler(MsisdnAlreadyRegisteredException.class)
    public ResponseEntity<String> handleMsisdnAlreadyRegistered(MsisdnAlreadyRegisteredException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidTariffException.class)
    public ResponseEntity<String> handleInvalidTariff(InvalidTariffException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleOtherExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error: " + ex.getMessage());
    }

    @ExceptionHandler(TariffNotFoundException.class)
    public ResponseEntity<String> handleTariffNotFound(TariffNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(TariffNotActiveException.class)
    public ResponseEntity<String> handleTariffNotActive(TariffNotActiveException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
