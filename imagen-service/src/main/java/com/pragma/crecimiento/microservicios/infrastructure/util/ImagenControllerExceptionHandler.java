package com.pragma.crecimiento.microservicios.infrastructure.util;


import com.pragma.crecimiento.microservicios.domain.exception.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
class ImagenControllerExceptionHandler{

    private static final Logger LOGGER = LoggerFactory.getLogger(ImagenControllerExceptionHandler.class);

    @ExceptionHandler(ImagenNoEncontradaException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleImagenNoEncontradaException(ImagenNoEncontradaException exception) {
        LOGGER.error(exception.getMessage());
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(exception.getMessage());
    }

    @ExceptionHandler(ImagenYaRegistradaException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleImagenYaRegistradaException(ImagenYaRegistradaException exception) {
        LOGGER.error(exception.getMessage());
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(exception.getMessage());
    }

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException exception) {
        LOGGER.error(exception.getReason());
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(exception.getReason());
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleInvalidDataAccessApiUsageException(InvalidDataAccessApiUsageException exception) {
        LOGGER.error(exception.getMessage());
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(exception.getMessage());
    }
}