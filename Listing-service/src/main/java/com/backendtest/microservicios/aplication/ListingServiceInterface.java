package com.backendtest.microservicios.aplication;

import java.util.List;
import java.util.UUID;

import com.backendtest.microservicios.domain.Listing;

public interface ListingServiceInterface {
    
    Listing registrar(Listing listing);

    Listing obtenerPorId(UUID id);

    Listing obtenerPorTipoIdentificacionNumeroIdentificacion(String tipoIdentificacion, String numeroIdentificacion);

    List<Listing> listarTodos();

    List<Listing> listarEdadMayorIgual(int edad);

    Listing actualizar(Listing listing);

    Listing eliminar(UUID id);
}
