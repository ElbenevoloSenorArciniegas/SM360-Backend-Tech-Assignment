package com.backendtest.microservicios.aplication;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.backendtest.microservicios.domain.Listing;
import com.backendtest.microservicios.domain.State;

public interface ListingRepositoryInterface {

    Listing save(Listing listing);

    Optional<Listing> findById(UUID id);

    List<Listing> findAll();

    void delete(Listing listing);

    List<Listing> findByDealerAndStateOrderByCreatedAt(UUID idDealer, State state);
    
}
