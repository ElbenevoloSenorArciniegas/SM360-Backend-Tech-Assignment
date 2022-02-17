package com.pragma.crecimiento.microservicios.infrastructure.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import org.springframework.validation.BindingResult;

public class ErrorMessagesFormatter {
    
    public static String formatearMensajesToString(BindingResult result){
        List<Map<String, String>> errores = result.getFieldErrors().stream()
            .map(err -> {
                Map<String, String> error = new HashMap<>();
                error.put(err.getField(), err.getDefaultMessage());
                return error;
            }).collect(Collectors.toList());
        
        Gson gson = new Gson();

        return gson.toJson(errores);
    }
}
