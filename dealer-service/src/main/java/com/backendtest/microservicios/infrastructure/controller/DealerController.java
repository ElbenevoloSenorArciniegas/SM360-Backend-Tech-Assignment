package com.backendtest.microservicios.infrastructure.controller;

import java.util.List;
import java.util.UUID;

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
    public ResponseEntity<List<Dealer>> getAll(){
        return ResponseEntity.status(HttpStatus.OK).body(dealerService.getAll());
    }

    @GetMapping("/{idDealer}")
    public ResponseEntity<Dealer> getById(@PathVariable(name = "idDealer") UUID idDealer){
        return ResponseEntity.status(HttpStatus.OK).body(dealerService.getById(idDealer));
    }

    @PostMapping("/")
    public ResponseEntity<Dealer> create(@Valid @RequestBody Dealer dealerRequest, BindingResult result){
        if(result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessagesFormatter.formatearMensajesToString(result));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(dealerService.create(dealerRequest));
    }
    
    @PutMapping("/")
    public ResponseEntity<Dealer> update(@Valid @RequestBody Dealer dealerRequest, BindingResult result){
        if(result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessagesFormatter.formatearMensajesToString(result));
        }
        return ResponseEntity.status(HttpStatus.OK).body(dealerService.update(dealerRequest));
    }

    @DeleteMapping("/{idDealer}")
    public ResponseEntity<Dealer> delete(@PathVariable(name = "idDealer") UUID idDealer){
        return ResponseEntity.status(HttpStatus.OK).body(dealerService.delete(idDealer));
    }
}
