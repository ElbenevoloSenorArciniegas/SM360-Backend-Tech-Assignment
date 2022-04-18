package com.backendtest.microservicios.infrastructure.repository.postgres.repository;

import java.util.Optional;
import java.util.UUID;

import com.backendtest.microservicios.infrastructure.repository.postgres.entity.DealerPostgresEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DealerPostgresRepository extends JpaRepository<DealerPostgresEntity, UUID>{
    
    Optional<DealerPostgresEntity> findById(UUID id);
}
