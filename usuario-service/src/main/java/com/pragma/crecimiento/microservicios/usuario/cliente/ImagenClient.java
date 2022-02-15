package com.pragma.crecimiento.microservicios.usuario.cliente;

import java.util.List;

import com.pragma.crecimiento.microservicios.usuario.model.Imagen;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "imagen-service", path = "/imagenes", fallback = ImagenHystrixFallbackFactory.class)
public interface ImagenClient {
    
    @GetMapping("/")
    public ResponseEntity<List<Imagen>> listarTodos();

    @GetMapping("/{idImagen}")
    public ResponseEntity<Imagen> obtenerPorId(@PathVariable(name = "idImagen") String idImagen);

    @PostMapping("/")
    public ResponseEntity<Imagen> registrar(Imagen imagenRequest);

    @PutMapping("/")
    public ResponseEntity<Imagen> actualizar(Imagen imagenRequest);

    @DeleteMapping("/{idImagen}")
    public ResponseEntity<Imagen> eliminar(@PathVariable(name = "idImagen") String idImagen);
}
