package com.backendtest.microservicios.aplication;

import java.util.List;
import java.util.UUID;

import com.backendtest.microservicios.domain.Listing;

public interface ListingRepositoryInterface {

    Listing save(Listing listing);

    Listing findById(UUID id);

    Listing findByTipoIdentificacionAndNumeroIdentificacion(String tipoIdentificacion,
            String numeroIdentificacion);

    List<Listing> findAll();

    List<Listing> findByEdadGreaterThanEqual(int edad);

    void delete(Listing listing);
    
}
