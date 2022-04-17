package com.pragma.backendtest.microservicios.infrastructure.repository.postgres.mapper;

import org.springframework.stereotype.Component;

import com.pragma.backendtest.microservicios.domain.Imagen;
import com.pragma.backendtest.microservicios.infrastructure.repository.postgres.entity.ImagenPostgresEntity;

import org.modelmapper.ModelMapper;

@Component
public class ImagenPostgresMapper{

  private ModelMapper mapper = new ModelMapper();

  public Imagen toDomain(ImagenPostgresEntity imagenEntity){
    return mapper.map(imagenEntity, Imagen.class);
  }

  public ImagenPostgresEntity toEntity(Imagen imagen){
    return mapper.map(imagen, ImagenPostgresEntity.class);
  }

}
