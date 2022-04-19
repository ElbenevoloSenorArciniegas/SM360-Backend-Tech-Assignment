package com.backendtest.microservicios.domain.exception;

public class TierLimitException extends RuntimeException{

    public TierLimitException(String message) {
        super(message);
    }
    
    
}
