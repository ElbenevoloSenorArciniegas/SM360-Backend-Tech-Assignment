package com.pragma.backendtest.microservicios.aplication.service;

import java.util.List;

import com.pragma.backendtest.microservicios.domain.Imagen;

public interface ImagenRepositoryInterface {

    Imagen save(Imagen imagen);

    Imagen findById(Long id);

    List<Imagen> findAll();

    void delete(Imagen imagen);
    

}
