package com.backendtest.microservicios.domain.exception;

public class DealerNotFoundException extends RuntimeException{

    public DealerNotFoundException(String message) {
        super(message);
    }
    
    
}
