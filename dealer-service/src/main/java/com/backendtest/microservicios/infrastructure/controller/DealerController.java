package com.backendtest.microservicios.infrastructure.controller;

import java.util.List;

import javax.validation.Valid;

import com.backendtest.microservicios.aplication.service.DealerServiceInterface;
import com.backendtest.microservicios.domain.Dealer;
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
@RequestMapping(value="/dealers")
public class DealerController {

    @Autowired
    private DealerServiceInterface dealerService;

    @GetMapping("/")
    public ResponseEntity<List<Dealer>> listarTodos(){
        return ResponseEntity.status(HttpStatus.OK).body(dealerService.listarTodos());
    }

    @GetMapping("/{idDealer}")
    public ResponseEntity<Dealer> obtenerPorId(@PathVariable(name = "idDealer") Long idDealer){
        return ResponseEntity.status(HttpStatus.OK).body(dealerService.obtenerPorId(idDealer));
    }

    @PostMapping("/")
    public ResponseEntity<Dealer> registrar(@Valid @RequestBody Dealer dealerRequest, BindingResult result){
        if(result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessagesFormatter.formatearMensajesToString(result));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(dealerService.registrar(dealerRequest));
    }
    
    @PutMapping("/")
    public ResponseEntity<Dealer> actualizar(@Valid @RequestBody Dealer dealerRequest, BindingResult result){
        if(result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessagesFormatter.formatearMensajesToString(result));
        }
        return ResponseEntity.status(HttpStatus.OK).body(dealerService.actualizar(dealerRequest));
    }

    @DeleteMapping("/{idDealer}")
    public ResponseEntity<Dealer> eliminar(@PathVariable(name = "idDealer") Long idDealer){
        return ResponseEntity.status(HttpStatus.OK).body(dealerService.eliminar(idDealer));
    }
}
