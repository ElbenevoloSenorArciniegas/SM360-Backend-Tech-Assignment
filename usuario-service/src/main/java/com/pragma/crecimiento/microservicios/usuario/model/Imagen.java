package com.pragma.crecimiento.microservicios.usuario.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Imagen{
    
    private Long id;
    private String data;
}
