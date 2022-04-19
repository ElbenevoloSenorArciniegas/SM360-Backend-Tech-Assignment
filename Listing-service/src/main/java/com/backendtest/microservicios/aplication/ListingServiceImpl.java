package com.backendtest.microservicios.aplication;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.logging.Logger;

import javax.transaction.Transactional;

import com.backendtest.microservicios.domain.Dealer;
import com.backendtest.microservicios.domain.Listing;
import com.backendtest.microservicios.domain.State;
import com.backendtest.microservicios.domain.exception.DealerNotFoundException;
import com.backendtest.microservicios.domain.exception.ListingNotFoundException;
import com.backendtest.microservicios.domain.exception.ListingYetRegistredException;
import com.backendtest.microservicios.infrastructure.cliente.DealerClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ListingServiceImpl implements ListingServiceInterface{

    Logger LOG = Logger.getLogger("ListingServiceImpl");

    @Autowired
    private ListingRepositoryInterface listingRepository;

    @Autowired
    private DealerClient dealerClient;

    @Override
    @Transactional
    public Listing create(Listing listing) {
        
        listing = searchDealer(listing);
        //No exception, great, continue
        listing.setCreatedAtNow();
        listing.setStateDefault();
        return listingRepository.save(listing);
    }

    @Override
    public Listing getById(UUID id){
        return listingRepository.findById(id);
        //return searchDealer(listingRepository.findById(id));
    }

    @Override
    public List<Listing> getAll() {
        return listingRepository.findAll();
        /*return listingRepository.findAll().stream().map(listing -> {
                return buscarDealer(listing); 
            }).collect(Collectors.toList());*/
    }

    @Override
    public List<Listing> getAllByDealerAndState(UUID dealerId, State state) {
        // TODO Auto-generated method stub
        return null;
        /*return listingRepository.findByEdadGreaterThanEqual(edad).stream().map(listing -> { 
            return buscarDealer(listing); 
        }).collect(Collectors.toList());*/
    }

    @Override
    public Listing update(Listing listing){
        getById(listing.getId()); 
        //Revisa si existe, si no, lanza excepción
        listing = searchDealer(listing);
        //No exception, great, continue
        return listingRepository.save(listing);
    }

    @Override
    @Transactional
    public Listing remove(UUID id){
        Listing listingRegistrado = getById(id);
        listingRepository.delete(listingRegistrado);
        return listingRegistrado;
    }

    private Listing searchDealer(Listing listing){
        if(listing.hasEmptyDealer()){
            Dealer dealerReturn = dealerClient.getById(listing.getDealer().getId()).getBody();
            listing.setDealer(dealerReturn);
        }
        return listing;
    }


    /*
        Please note that the number of published listings for a dealer should be less or equal to the dealer's tier limit. 
        When publishing a listing, client should be able to choose how to deal with situation when tier limit is reached - either
            * return an error to the client, 
            * or publish a listing, but unpublish the oldest listing of a dealer to conform to the tier limit.
    */
    @Override
    public Listing publish(UUID id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Listing unpublish(UUID id) {
        // TODO Auto-generated method stub
        return null;
    }
    
    
}
