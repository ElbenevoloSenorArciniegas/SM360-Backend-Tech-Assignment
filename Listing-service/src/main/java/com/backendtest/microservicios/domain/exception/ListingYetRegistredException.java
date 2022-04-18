package com.backendtest.microservicios.domain.exception;

public class ListingYetRegistredException extends RuntimeException{

    public ListingYetRegistredException(String message) {
        super(message);
    }
    
    
}
