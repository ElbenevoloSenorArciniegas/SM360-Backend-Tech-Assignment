package com.backendtest.microservicios.infrastructure.cliente;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.backendtest.microservicios.domain.Dealer;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class DealerHystrixFallbackFactory implements DealerClient{

    private Dealer defaultNoneDealer = Dealer.getDefaultResponse();

    @Override
    public ResponseEntity<List<Dealer>> getAll() {
        return ResponseEntity.ok(new ArrayList<>());
    }

    @Override
    public ResponseEntity<Dealer> getById(UUID idDealer) {
        return ResponseEntity.ok(defaultNoneDealer);
    }

    @Override
    public ResponseEntity<Dealer> create(Dealer dealerRequest) {
        return ResponseEntity.ok(defaultNoneDealer);
    }

    @Override
    public ResponseEntity<Dealer> update(Dealer dealerRequest) {
        return ResponseEntity.ok(defaultNoneDealer);
    }

    @Override
    public ResponseEntity<Dealer> remove(UUID idDealer) {
        return ResponseEntity.ok(defaultNoneDealer);
    }
    
}
