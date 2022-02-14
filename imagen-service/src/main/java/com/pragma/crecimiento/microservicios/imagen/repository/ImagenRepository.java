package com.pragma.crecimiento.microservicios.imagen.repository;

import com.pragma.crecimiento.microservicios.imagen.entity.Imagen;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagenRepository extends MongoRepository<Imagen, Long>{
    
}
