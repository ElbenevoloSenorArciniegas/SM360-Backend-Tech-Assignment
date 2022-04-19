package com.backendtest.microservicios.infrastructure.repository.postgres.mediator;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.backendtest.microservicios.aplication.service.DealerRepositoryInterface;
import com.backendtest.microservicios.domain.Dealer;
import com.backendtest.microservicios.infrastructure.repository.postgres.mapper.DealerPostgresMapper;
import com.backendtest.microservicios.infrastructure.repository.postgres.repository.DealerPostgresRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DealerPostgresMediator implements DealerRepositoryInterface{

    Logger LOG = Logger.getLogger("DealerPostgresMediator");

    @Autowired
    private DealerPostgresRepository dealerRepository;

    @Autowired
    private DealerPostgresMapper dealerMapper;

    @Override
    public Dealer save(Dealer dealer) {
        return dealerMapper.toDomain(dealerRepository.save(dealerMapper.toEntity(dealer)));
    }

    @Override
    public Optional<Dealer> findById(UUID id) {
        return dealerRepository.findById(id)
            .map((dealerEntity) -> dealerMapper.toDomain(dealerEntity));
    }

    @Override
    public List<Dealer> findAll() {
        return dealerRepository.findAll().stream().map(
            dealerEntity -> dealerMapper.toDomain(dealerEntity)
        ).collect(Collectors.toList());
    }

    @Override
    public void delete(Dealer dealer) {
        dealerRepository.delete(dealerMapper.toEntity(dealer));        
    }
    
}
