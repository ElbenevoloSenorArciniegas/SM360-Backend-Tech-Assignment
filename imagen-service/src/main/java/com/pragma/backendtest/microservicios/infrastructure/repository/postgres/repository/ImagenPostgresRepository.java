package com.pragma.backendtest.microservicios.infrastructure.repository.postgres.repository;

import java.util.Optional;

import com.pragma.backendtest.microservicios.infrastructure.repository.postgres.entity.ImagenPostgresEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagenPostgresRepository extends JpaRepository<ImagenPostgresEntity, Long>{
    
    Optional<ImagenPostgresEntity> findById(Long id);
}
