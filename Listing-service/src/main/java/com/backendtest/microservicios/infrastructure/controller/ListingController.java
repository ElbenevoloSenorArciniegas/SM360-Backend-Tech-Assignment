package com.backendtest.microservicios.infrastructure.controller;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import com.backendtest.microservicios.aplication.ListingServiceInterface;
import com.backendtest.microservicios.domain.Listing;
import com.backendtest.microservicios.infrastructure.util.ErrorMessagesFormatter;

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
@RequestMapping(value="/listings")
public class ListingController {

    @Autowired
    private ListingServiceInterface listingService;

    @GetMapping("/")
    public ResponseEntity<List<Listing>> listarTodos(){
        return ResponseEntity.status(HttpStatus.OK).body(listingService.listarTodos());
    }

    @GetMapping("/{idListing}")
    public ResponseEntity<Listing> obtenerPorId(@PathVariable(name = "idListing") UUID idListing){
        return ResponseEntity.status(HttpStatus.OK).body(listingService.obtenerPorId(idListing));
    }

    @GetMapping("/documento/{tipo}/{numero}/")
    public ResponseEntity<Listing> obtenerPorTipoIdentificacionNumeroIdentificacion(@PathVariable(name = "tipo") String tipo, @PathVariable(name = "numero") String numero){
        return ResponseEntity.status(HttpStatus.OK).body(listingService.obtenerPorTipoIdentificacionNumeroIdentificacion(tipo, numero));
    }

    @GetMapping("/edadMayorIgualA/{edadMinima}")
    public ResponseEntity<List<Listing>> listarEdadMayorIgual(@PathVariable(name = "edadMinima") int edadMinima){
        return ResponseEntity.status(HttpStatus.OK).body(listingService.listarEdadMayorIgual(edadMinima));
    }

    @PostMapping("/")
    public ResponseEntity<Listing> registrar(@Valid @RequestBody Listing listingRequest, BindingResult result){
        if(result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessagesFormatter.formatearMensajesToString(result));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(listingService.registrar(listingRequest));
    }
    
    @PutMapping("/")
    public ResponseEntity<Listing> actualizar(@Valid @RequestBody Listing listingRequest, BindingResult result){
        if(result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessagesFormatter.formatearMensajesToString(result));
        }
        return ResponseEntity.status(HttpStatus.OK).body(listingService.actualizar(listingRequest));
    }

    @DeleteMapping("/{idListing}")
    public ResponseEntity<Listing> eliminar(@PathVariable(name = "idListing") UUID idListing){
        return ResponseEntity.status(HttpStatus.OK).body(listingService.eliminar(idListing));
    }
}
