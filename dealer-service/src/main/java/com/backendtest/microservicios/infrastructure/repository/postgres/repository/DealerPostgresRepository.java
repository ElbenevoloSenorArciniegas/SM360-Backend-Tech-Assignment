package com.backendtest.microservicios.infrastructure.repository.postgres.repository;

import java.util.Optional;

import com.backendtest.microservicios.infrastructure.repository.postgres.entity.DealerPostgresEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DealerPostgresRepository extends JpaRepository<DealerPostgresEntity, Long>{
    
    Optional<DealerPostgresEntity> findById(Long id);
}
