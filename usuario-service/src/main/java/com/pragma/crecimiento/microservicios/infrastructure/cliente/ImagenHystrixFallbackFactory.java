package com.pragma.crecimiento.microservicios.infrastructure.cliente;

import java.util.ArrayList;
import java.util.List;

import com.pragma.crecimiento.microservicios.domain.Imagen;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ImagenHystrixFallbackFactory implements ImagenClient{

    @Override
    public ResponseEntity<List<Imagen>> listarTodos() {
        return ResponseEntity.ok(new ArrayList<>());
    }

    @Override
    public ResponseEntity<Imagen> obtenerPorId(String idImagen) {
        return ResponseEntity.ok(
            Imagen.builder().id("0").data("none").build()
        );
    }

    @Override
    public ResponseEntity<Imagen> registrar(Imagen imagenRequest) {
        return ResponseEntity.ok(
            Imagen.builder().id("0").data("none").build()
        );
    }

    @Override
    public ResponseEntity<Imagen> actualizar(Imagen imagenRequest) {
        return ResponseEntity.ok(
            Imagen.builder().id("0").data("none").build()
        );
    }

    @Override
    public ResponseEntity<Imagen> eliminar(String idImagen) {
        return ResponseEntity.ok(
            Imagen.builder().id("0").data("none").build()
        );
    }
    
}
