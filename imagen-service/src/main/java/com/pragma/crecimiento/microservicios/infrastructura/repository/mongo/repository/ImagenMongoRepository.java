package com.pragma.crecimiento.microservicios.infrastructura.repository.mongo.repository;

import java.util.Optional;

import com.pragma.crecimiento.microservicios.infrastructura.repository.mongo.entity.ImagenMongoEntity;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagenMongoRepository extends MongoRepository<ImagenMongoEntity, Long>{
    
    @Query("{id:'?0'}")
    Optional<ImagenMongoEntity> findById(String id);
}
