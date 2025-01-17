package com.backendtest.microservicios.infrastructure.controller;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import com.backendtest.microservicios.aplication.ListingServiceInterface;
import com.backendtest.microservicios.domain.Listing;
import com.backendtest.microservicios.domain.PublicationMethod;
import com.backendtest.microservicios.domain.State;
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
    public ResponseEntity<List<Listing>> getAll(){
        return ResponseEntity.status(HttpStatus.OK).body(listingService.getAll());
    }

    @GetMapping("/{idListing}")
    public ResponseEntity<Listing> getById(@PathVariable(name = "idListing") UUID idListing){
        return ResponseEntity.status(HttpStatus.OK).body(listingService.getById(idListing));
    }

    @GetMapping("/{idDealer}/{state}")
    public ResponseEntity<List<Listing>> getAllByDealerAndState(@PathVariable(name = "idDealer") UUID idDealer, @PathVariable(name = "state") State state){
        return ResponseEntity.status(HttpStatus.OK).body(listingService.getAllByDealerAndState(idDealer, state));
    }

    @PostMapping("/")
    public ResponseEntity<Listing> create(@Valid @RequestBody Listing listingRequest, BindingResult result){
        if(result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessagesFormatter.formatearMensajesToString(result));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(listingService.create(listingRequest));
    }
    
    @PutMapping("/")
    public ResponseEntity<Listing> update(@Valid @RequestBody Listing listingRequest, BindingResult result){
        if(result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessagesFormatter.formatearMensajesToString(result));
        }
        return ResponseEntity.status(HttpStatus.OK).body(listingService.update(listingRequest));
    }

    @PutMapping("/public/{idListing}/{publicationMethod}")
    public ResponseEntity<Listing> publish(@PathVariable(name = "idListing") UUID idListing, @PathVariable(name = "publicationMethod") PublicationMethod publicationMethod){
        return ResponseEntity.status(HttpStatus.OK).body(listingService.publish(idListing, publicationMethod));
    }

    @PutMapping("/draft/{idListing}")
    public ResponseEntity<Listing> unpublish(@PathVariable(name = "idListing") UUID idListing){
        return ResponseEntity.status(HttpStatus.OK).body(listingService.unpublish(idListing));
    }

    @PutMapping("/tierLimit/{tierLimit}")
    public ResponseEntity<String> changeTierLimit(@PathVariable(name = "tierLimit") int tierLimit){
        listingService.changeTierLimit(tierLimit);
        return ResponseEntity.status(HttpStatus.OK).body("New Tier limit set to "+tierLimit);
    }

    @DeleteMapping("/{idListing}")
    public ResponseEntity<Listing> remove(@PathVariable(name = "idListing") UUID idListing){
        return ResponseEntity.status(HttpStatus.OK).body(listingService.remove(idListing));
    }
}
