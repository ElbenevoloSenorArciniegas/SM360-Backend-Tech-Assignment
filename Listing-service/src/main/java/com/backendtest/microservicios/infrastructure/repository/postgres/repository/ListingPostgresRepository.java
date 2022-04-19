package com.backendtest.microservicios.infrastructure.repository.postgres.repository;


import java.util.List;
import java.util.UUID;

import com.backendtest.microservicios.domain.State;
import com.backendtest.microservicios.infrastructure.repository.postgres.entity.ListingPostgresEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ListingPostgresRepository extends JpaRepository<ListingPostgresEntity, UUID>{

    @Query("SELECT new ListingPostgresEntity(l.id, l.vehicle, l.price, l.createdAt, l.state, l.dealerId) "
        + " FROM ListingPostgresEntity l "
        + " WHERE l.dealerId = :dealerId AND l.state = :state "
        + " ORDER BY l.createdAt ")
    List<ListingPostgresEntity> findByDealerAndStateOrderByCreatedAt(@Param("dealerId") UUID dealerId, @Param("state") State state);

    
}
