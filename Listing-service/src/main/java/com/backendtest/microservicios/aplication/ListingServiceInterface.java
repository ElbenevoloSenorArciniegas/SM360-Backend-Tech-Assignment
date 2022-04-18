package com.backendtest.microservicios.aplication;

import java.util.List;

import com.backendtest.microservicios.domain.Listing;

public interface ListingServiceInterface {
    
    Listing registrar(Listing listing);

    Listing obtenerPorId(Long id);

    Listing obtenerPorTipoIdentificacionNumeroIdentificacion(String tipoIdentificacion, String numeroIdentificacion);

    List<Listing> listarTodos();

    List<Listing> listarEdadMayorIgual(int edad);

    Listing actualizar(Listing listing);

    Listing eliminar(Long id);
}
