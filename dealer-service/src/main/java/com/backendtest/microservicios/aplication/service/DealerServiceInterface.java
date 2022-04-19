package com.backendtest.microservicios.aplication.service;

import java.util.List;
import java.util.UUID;

import com.backendtest.microservicios.domain.Dealer;

public interface DealerServiceInterface {
    
    Dealer create(Dealer dealer);

    Dealer getById(UUID id);

    List<Dealer> getAll();

    Dealer update(Dealer dealer);

    Dealer delete(UUID id);
}
