package com.backendtest.microservicios.aplication.service;

import java.util.List;
import java.util.UUID;

import com.backendtest.microservicios.domain.Dealer;

public interface DealerRepositoryInterface {

    Dealer save(Dealer dealer);

    Dealer findById(UUID id);

    List<Dealer> findAll();

    void delete(Dealer dealer);
    

}
