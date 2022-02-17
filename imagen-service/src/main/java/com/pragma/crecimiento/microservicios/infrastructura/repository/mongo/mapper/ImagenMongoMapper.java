package com.pragma.crecimiento.microservicios.infrastructura.repository.mongo.mapper;

import com.pragma.crecimiento.microservicios.domain.Imagen;
import com.pragma.crecimiento.microservicios.infrastructura.repository.mongo.entity.ImagenMongoEntity;

import org.springframework.stereotype.Component;
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
