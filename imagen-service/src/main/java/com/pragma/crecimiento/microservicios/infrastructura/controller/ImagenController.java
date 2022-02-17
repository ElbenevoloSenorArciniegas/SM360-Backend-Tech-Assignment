package com.pragma.crecimiento.microservicios.infrastructura.controller;

import java.util.List;

import javax.validation.Valid;

import com.pragma.crecimiento.microservicios.aplication.service.ImagenServiceInterface;
import com.pragma.crecimiento.microservicios.domain.Imagen;
import com.pragma.crecimiento.microservicios.infrastructura.util.ErrorMessagesFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value="/imagenes")
public class ImagenController {

    @Autowired
    private ImagenServiceInterface imagenService;

    @GetMapping("/")
    public ResponseEntity<List<Imagen>> listarTodos(){
        return ResponseEntity.status(HttpStatus.OK).body(imagenService.listarTodos());
    }

    @GetMapping("/{idImagen}")
    public ResponseEntity<Imagen> obtenerPorId(@PathVariable(name = "idImagen") String idImagen){
        return ResponseEntity.status(HttpStatus.OK).body(imagenService.obtenerPorId(idImagen));
    }

    @PostMapping("/")
    public ResponseEntity<Imagen> registrar(@Valid @RequestBody Imagen imagenRequest, BindingResult result){
        if(result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessagesFormatter.formatearMensajesToString(result));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(imagenService.registrar(imagenRequest));
    }
    
    @PutMapping("/")
    public ResponseEntity<Imagen> actualizar(@Valid @RequestBody Imagen imagenRequest, BindingResult result){
        if(result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessagesFormatter.formatearMensajesToString(result));
        }
        return ResponseEntity.status(HttpStatus.OK).body(imagenService.actualizar(imagenRequest));
    }

    @DeleteMapping("/{idImagen}")
    public ResponseEntity<Imagen> eliminar(@PathVariable(name = "idImagen") String idImagen){
        return ResponseEntity.status(HttpStatus.OK).body(imagenService.eliminar(idImagen));
    }
}
