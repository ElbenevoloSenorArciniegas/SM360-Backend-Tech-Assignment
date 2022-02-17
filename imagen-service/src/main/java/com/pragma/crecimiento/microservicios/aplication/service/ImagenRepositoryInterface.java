package com.pragma.crecimiento.microservicios.aplication.service;

import java.util.List;

import com.pragma.crecimiento.microservicios.domain.Imagen;

public interface ImagenRepositoryInterface {

    Imagen save(Imagen imagen);

    Imagen findById(String id);

    List<Imagen> findAll();

    void delete(Imagen imagen);
    

}
