package com.pragma.crecimiento.microservicios.imagen.repository;

import java.util.Optional;

import com.pragma.crecimiento.microservicios.imagen.entity.Imagen;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagenRepository extends MongoRepository<Imagen, Long>{
    
    @Query("{id:'?0'}")
    Optional<Imagen> findById(String id);
}
