package com.backendtest.microservicios.aplication.service;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import com.backendtest.microservicios.domain.Dealer;
import com.backendtest.microservicios.domain.exception.DealerNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DealerServiceImpl implements DealerServiceInterface{

    Logger LOG = Logger.getLogger("DealerServiceImpl");

    @Autowired
    private DealerRepositoryInterface dealerRepository;

    @Override
    @Transactional
    public Dealer create(Dealer dealer) {
        dealer.removeId(); //I need it? Dunno, I don't think so, but...
        //TODO how to check if a dealer already exist and not creating copies
        return dealerRepository.save(dealer);
    }

    @Override
    public Dealer getById(UUID id){
        return dealerRepository.findById(id)
            .orElseThrow(() -> new DealerNotFoundException("Not found Dealer with id "+id));
    }

    @Override
    public List<Dealer> getAll() {
        return dealerRepository.findAll();
    }

    @Override
    public Dealer update(Dealer dealer){
        getById(dealer.getId()); //Check if exists. If not found, throws exception
        return dealerRepository.save(dealer);
    }

    @Override
    public Dealer delete(UUID id){
        Dealer dealerRegistrada = getById(id); //Check if exists. If not found, throws exception
        dealerRepository.delete(dealerRegistrada);
        return dealerRegistrada;
    }

    
    
}
