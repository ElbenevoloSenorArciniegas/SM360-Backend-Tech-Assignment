package com.pragma.crecimiento.microservicios.imagen.entity;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("imagenes")
@Data 
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Imagen implements Serializable{
    
    @Id
    private String id;
    
    @NotBlank
    private String data;
}
