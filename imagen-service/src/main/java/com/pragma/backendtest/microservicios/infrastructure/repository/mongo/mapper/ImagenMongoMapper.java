package com.pragma.backendtest.microservicios.infrastructure.repository.mongo.mapper;

import org.springframework.stereotype.Component;

import com.pragma.backendtest.microservicios.domain.Imagen;
import com.pragma.backendtest.microservicios.infrastructure.repository.mongo.entity.ImagenMongoEntity;

import org.modelmapper.ModelMapper;

@Component
public class ImagenMongoMapper{

  private ModelMapper mapper = new ModelMapper();

  public Imagen toDomain(ImagenMongoEntity imagenEntity){
    return mapper.map(imagenEntity, Imagen.class);
  }

  public ImagenMongoEntity toEntity(Imagen imagen){
    return mapper.map(imagen, ImagenMongoEntity.class);
  }

}
