package com.backendtest.microservicios.aplication.service;

import java.util.List;

import com.backendtest.microservicios.domain.Dealer;

public interface DealerRepositoryInterface {

    Dealer save(Dealer dealer);

    Dealer findById(Long id);

    List<Dealer> findAll();

    void delete(Dealer dealer);
    

}
