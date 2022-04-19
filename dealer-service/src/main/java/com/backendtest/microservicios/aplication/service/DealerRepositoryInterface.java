package com.backendtest.microservicios.aplication.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.backendtest.microservicios.domain.Dealer;

public interface DealerRepositoryInterface {

    Dealer save(Dealer dealer);

    Optional<Dealer> findById(UUID id);

    List<Dealer> findAll();

    void delete(Dealer dealer);
    

}
