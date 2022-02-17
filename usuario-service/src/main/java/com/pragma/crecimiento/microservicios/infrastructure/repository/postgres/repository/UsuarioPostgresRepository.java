package com.pragma.crecimiento.microservicios.infrastructure.repository.postgres.repository;


import java.util.List;
import java.util.Optional;

import com.pragma.crecimiento.microservicios.infrastructure.repository.postgres.entity.UsuarioPostgresEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioPostgresRepository extends JpaRepository<UsuarioPostgresEntity, Long>{
    
    Optional<UsuarioPostgresEntity> findByTipoIdentificacionAndNumeroIdentificacion(String tipoIdentificacion, String numeroIdentificacion);

    List<UsuarioPostgresEntity> findByEdadGreaterThanEqual(int edad);

    
}
