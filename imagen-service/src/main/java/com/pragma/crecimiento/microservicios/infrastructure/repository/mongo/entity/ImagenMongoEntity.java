package com.pragma.crecimiento.microservicios.infrastructure.repository.mongo.entity;

import java.io.Serializable;

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
public class ImagenMongoEntity implements Serializable{
    
    @Id
    private String id;
    private String data;
}
