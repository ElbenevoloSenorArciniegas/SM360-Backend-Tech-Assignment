package com.backendtest.microservicios.infrastructure.cliente;

import java.util.ArrayList;
import java.util.List;

import com.backendtest.microservicios.domain.Dealer;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class DealerHystrixFallbackFactory implements DealerClient{

    @Override
    public ResponseEntity<List<Dealer>> listarTodos() {
        return ResponseEntity.ok(new ArrayList<>());
    }

    @Override
    public ResponseEntity<Dealer> obtenerPorId(String idImagen) {
        return ResponseEntity.ok(
            Dealer.builder().id("0").data("none").build()
        );
    }

    @Override
    public ResponseEntity<Dealer> registrar(Dealer dealerRequest) {
        return ResponseEntity.ok(
            Dealer.builder().id("0").data("none").build()
        );
    }

    @Override
    public ResponseEntity<Dealer> actualizar(Dealer dealerRequest) {
        return ResponseEntity.ok(
            Dealer.builder().id("0").data("none").build()
        );
    }

    @Override
    public ResponseEntity<Dealer> eliminar(String idImagen) {
        return ResponseEntity.ok(
            Dealer.builder().id("0").data("none").build()
        );
    }
    
}
