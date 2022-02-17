package com.pragma.crecimiento.microservicios.domain.exception;

public class UsuarioYaRegistradoException extends RuntimeException{

    public UsuarioYaRegistradoException(String message) {
        super(message);
    }
    
    
}
