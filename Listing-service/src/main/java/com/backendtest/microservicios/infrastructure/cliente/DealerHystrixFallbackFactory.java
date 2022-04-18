package com.backendtest.microservicios.infrastructure.cliente;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.backendtest.microservicios.domain.Dealer;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class DealerHystrixFallbackFactory implements DealerClient{

    private Dealer defaultNoneDealer = Dealer.builder().id(UUID.fromString("none")).name("none").build();

    @Override
    public ResponseEntity<List<Dealer>> listarTodos() {
        return ResponseEntity.ok(new ArrayList<>());
    }

    @Override
    public ResponseEntity<Dealer> obtenerPorId(UUID idDealer) {
        return ResponseEntity.ok(defaultNoneDealer);
    }

    @Override
    public ResponseEntity<Dealer> registrar(Dealer dealerRequest) {
        return ResponseEntity.ok(defaultNoneDealer);
    }

    @Override
    public ResponseEntity<Dealer> actualizar(Dealer dealerRequest) {
        return ResponseEntity.ok(defaultNoneDealer);
    }

    @Override
    public ResponseEntity<Dealer> eliminar(UUID idDealer) {
        return ResponseEntity.ok(defaultNoneDealer);
    }
    
}
