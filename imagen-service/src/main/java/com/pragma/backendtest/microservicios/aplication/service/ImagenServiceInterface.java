package com.pragma.backendtest.microservicios.aplication.service;

import java.util.List;

import com.pragma.backendtest.microservicios.domain.Imagen;

public interface ImagenServiceInterface {
    
    Imagen registrar(Imagen imagen);

    Imagen obtenerPorId(Long id);

    List<Imagen> listarTodos();

    Imagen actualizar(Imagen imagen);

    Imagen eliminar(Long id);
}
