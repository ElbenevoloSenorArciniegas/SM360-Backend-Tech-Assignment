package com.backendtest.microservicios.infrastructure.cliente;

import java.util.List;
import java.util.UUID;

import com.backendtest.microservicios.domain.Dealer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "dealer-service", path = "/dealers", fallback = DealerHystrixFallbackFactory.class)
public interface DealerClient {
    
    @GetMapping("/")
    public ResponseEntity<List<Dealer>> getAll();

    @GetMapping("/{idDealer}")
    public ResponseEntity<Dealer> getById(@PathVariable(name = "idDealer") UUID idDealer);

    @PostMapping("/")
    public ResponseEntity<Dealer> create(Dealer dealerRequest);

    @PutMapping("/")
    public ResponseEntity<Dealer> update(Dealer dealerRequest);

    @DeleteMapping("/{idDealer}")
    public ResponseEntity<Dealer> remove(@PathVariable(name = "idDealer") UUID idDealer);
}
