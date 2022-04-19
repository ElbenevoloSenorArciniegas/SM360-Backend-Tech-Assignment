package com.backendtest.microservicios.aplication;

import java.util.List;
import java.util.UUID;

import com.backendtest.microservicios.domain.Listing;
import com.backendtest.microservicios.domain.State;

public interface ListingServiceInterface {
    
    Listing create(Listing listing);

    Listing getById(UUID id);

    List<Listing> getAll();

    List<Listing> getAllByDealerAndState(UUID dealerId, State state);

    Listing update(Listing listing);

    Listing remove(UUID id);

    Listing publish(UUID id);

    Listing unpublish(UUID id);
}
