package com.pragma.crecimiento.microservicios.domain.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class ImagenNoEncontradaException extends WebApplicationException{

    public ImagenNoEncontradaException(String message) {
        super(Response.status(Status.BAD_REQUEST).header("reason", message).build());
    }
    
    
}
