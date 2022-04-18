package com.backendtest.microservicios.aplication.service;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import com.backendtest.microservicios.domain.Dealer;
import com.backendtest.microservicios.domain.exception.DealerNoEncontradoException;
import com.backendtest.microservicios.domain.exception.DealerYaRegistradaException;

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
    public Dealer registrar(Dealer dealer) {
        try {
            obtenerPorId(dealer.getId());
            throw new DealerYaRegistradaException("Ya existe una dealer registrada con el id "+dealer.getId());
        } catch (DealerNoEncontradoException e) {
            dealer.setId(null); //Evita que se guarden las imágenes con id 0. Obliga a autogenerar un id
            return dealerRepository.save(dealer);
        }
    }

    @Override
    public Dealer obtenerPorId(UUID id){
        return dealerRepository.findById(id);
    }

    @Override
    public List<Dealer> listarTodos() {
        return dealerRepository.findAll();
    }

    @Override
    public Dealer actualizar(Dealer dealer){
        obtenerPorId(dealer.getId()); //Revisa si existe, si no, lanza excepción
        return dealerRepository.save(dealer);
    }

    @Override
    public Dealer eliminar(UUID id){
        Dealer dealerRegistrada = obtenerPorId(id); //Revisa si existe, si no, lanza excepción
        dealerRepository.delete(dealerRegistrada);
        return dealerRegistrada;
    }

    
    
}
