package com.backendtest.microservicios.aplication.service;

import java.util.List;
import java.util.UUID;

import com.backendtest.microservicios.domain.Dealer;

public interface DealerServiceInterface {
    
    Dealer registrar(Dealer dealer);

    Dealer obtenerPorId(UUID id);

    List<Dealer> listarTodos();

    Dealer actualizar(Dealer dealer);

    Dealer eliminar(UUID id);
}
