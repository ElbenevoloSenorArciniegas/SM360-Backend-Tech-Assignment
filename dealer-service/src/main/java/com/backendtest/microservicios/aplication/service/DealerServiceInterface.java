package com.backendtest.microservicios.aplication.service;

import java.util.List;

import com.backendtest.microservicios.domain.Dealer;

public interface DealerServiceInterface {
    
    Dealer registrar(Dealer dealer);

    Dealer obtenerPorId(Long id);

    List<Dealer> listarTodos();

    Dealer actualizar(Dealer dealer);

    Dealer eliminar(Long id);
}
