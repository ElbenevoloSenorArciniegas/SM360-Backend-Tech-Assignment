package com.backendtest.microservicios.infrastructure.util;

import com.backendtest.microservicios.domain.exception.*;

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
class ListingControllerExceptionHandler{

    private static final Logger LOGGER = LoggerFactory.getLogger(ListingControllerExceptionHandler.class);

    @ExceptionHandler(ListingNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleListingNotFoundException(ListingNotFoundException exception) {
        LOGGER.error(exception.getMessage());
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(exception.getMessage());
    }
    
    @ExceptionHandler(DealerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleDealerNotFoundException(DealerNotFoundException exception) {
        LOGGER.error(exception.getMessage());
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(exception.getMessage());
    }

    @ExceptionHandler(TierLimitException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ResponseEntity<String> handleTierLimitException(TierLimitException exception) {
        LOGGER.error(exception.getMessage());
        return ResponseEntity
            .status(HttpStatus.NOT_ACCEPTABLE)
            .body(exception.getMessage());
    }

    @ExceptionHandler(DealerClientNotAviable.class)
    @ResponseStatus(HttpStatus.FAILED_DEPENDENCY)
    public ResponseEntity<String> handleDealerClientNotAviable(DealerClientNotAviable exception) {
        LOGGER.error(exception.getMessage());
        return ResponseEntity
            .status(HttpStatus.FAILED_DEPENDENCY)
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