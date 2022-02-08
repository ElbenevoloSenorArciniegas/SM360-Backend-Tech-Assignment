package com.pragma.crecimiento.microservicios.usuario.exception;

public class UsuarioYaRegistradoException extends RuntimeException{

    public UsuarioYaRegistradoException(String message) {
        super(message);
    }
    
    
}
