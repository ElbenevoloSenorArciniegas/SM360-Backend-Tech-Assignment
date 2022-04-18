package com.backendtest.microservicios.infrastructure.repository.postgres.repository;


import java.util.List;
import java.util.Optional;

import com.backendtest.microservicios.infrastructure.repository.postgres.entity.ListingPostgresEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListingPostgresRepository extends JpaRepository<ListingPostgresEntity, Long>{
    
    Optional<ListingPostgresEntity> findByTipoIdentificacionAndNumeroIdentificacion(String tipoIdentificacion, String numeroIdentificacion);

    List<ListingPostgresEntity> findByEdadGreaterThanEqual(int edad);

    
}
