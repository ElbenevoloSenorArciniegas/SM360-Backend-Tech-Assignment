package com.pragma.backendtest.microservicios.domain;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Imagen{
    
    private Long id;

    @NotBlank
    private String data;
}
