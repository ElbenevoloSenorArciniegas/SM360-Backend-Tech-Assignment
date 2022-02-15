package com.pragma.crecimiento.microservicios.imagen.service;

import java.util.List;

import com.pragma.crecimiento.microservicios.imagen.entity.Imagen;

public interface ImagenServiceInterface {
    
    Imagen registrar(Imagen imagen);

    Imagen obtenerPorId(String id);

    List<Imagen> listarTodos();

    Imagen actualizar(Imagen imagen);

    Imagen eliminar(String id);
}
