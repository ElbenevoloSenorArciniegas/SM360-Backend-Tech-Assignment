package com.backendtest.microservicios.infrastructure.cliente;

import java.util.List;
import java.util.UUID;

import com.backendtest.microservicios.domain.Dealer;
import com.backendtest.microservicios.domain.exception.DealerClientNotAviable;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class DealerHystrixFallbackFactory implements DealerClient{

    @Override
    public ResponseEntity<List<Dealer>> getAll() {
        throw new DealerClientNotAviable("Dealer service is not aviable");
    }

    @Override
    public ResponseEntity<Dealer> getById(UUID idDealer) {
        throw new DealerClientNotAviable("Dealer service is not aviable");
    }

    @Override
    public ResponseEntity<Dealer> create(Dealer dealerRequest) {
        throw new DealerClientNotAviable("Dealer service is not aviable");
    }

    @Override
    public ResponseEntity<Dealer> update(Dealer dealerRequest) {
        throw new DealerClientNotAviable("Dealer service is not aviable");
    }

    @Override
    public ResponseEntity<Dealer> remove(UUID idDealer) {
        throw new DealerClientNotAviable("Dealer service is not aviable");
    }
    
}
