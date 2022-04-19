package com.backendtest.microservicios.aplication;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import javax.transaction.Transactional;

import com.backendtest.microservicios.domain.Dealer;
import com.backendtest.microservicios.domain.Listing;
import com.backendtest.microservicios.domain.PublicationMethod;
import com.backendtest.microservicios.domain.State;
import com.backendtest.microservicios.domain.exception.ListingNotFoundException;
import com.backendtest.microservicios.domain.exception.PublicationMethodNotSuportedException;
import com.backendtest.microservicios.domain.exception.TierLimitException;
import com.backendtest.microservicios.infrastructure.cliente.DealerClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ListingServiceImpl implements ListingServiceInterface{

    Logger LOG = Logger.getLogger("ListingServiceImpl");

    @Value( "${listing.tierLimit}" )
    private int TIER_LIMIT;

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
        return this.save(listing);
    }

    @Override
    public Listing getById(UUID id){
        return listingRepository.findById(id)
            .orElseThrow(() -> new ListingNotFoundException("Not found Listing with id "+id));
    }

    @Override
    public List<Listing> getAll() {
        return listingRepository.findAll();
    }

    /*
        MUST ORDER BY DATE FROM NEWER TO OLDER
    */
    @Override
    public List<Listing> getAllByDealerAndState(UUID dealerId, State state) {
        return listingRepository.findByDealerAndStateOrderByCreatedAt(dealerId, state);
    }

    @Override
    public Listing update(Listing listing){
        this.getById(listing.getId()); 
        //Revisa si existe, si no, lanza excepción
        listing = searchDealer(listing);
        //No exception, great, continue
        listing.setCreatedAtNow();
        return this.save(listing);
    }

    @Override
    @Transactional
    public Listing remove(UUID id){
        Listing listingRegistrado = this.getById(id);
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
    public Listing publish(UUID id, PublicationMethod publicationMethod) {
        
        Listing listingRegistrado = this.getById(id);

        List<Listing> currentPublishedList = this.getAllByDealerAndState(listingRegistrado.getDealer().getId(), State.PUBLISHED);

        if(currentPublishedList.size() == TIER_LIMIT){
            this.resolvePublication(currentPublishedList, publicationMethod);
        }

        listingRegistrado.publish();
        listingRegistrado = this.save(listingRegistrado);

        return listingRegistrado;
    }

    private void resolvePublication(List<Listing> currentPublishedList, PublicationMethod publicationMethod){
        
        if(publicationMethod == PublicationMethod.THROWS_ERROR){
            throw new TierLimitException("Max Tier limit");
        }

        if(publicationMethod == PublicationMethod.REPLACE_LAST){
            Listing last = currentPublishedList.get(TIER_LIMIT - 1);
            this.saveUnpublish(last);
            return;
        }

        throw new PublicationMethodNotSuportedException("The method for publication conflicts is not supported");
    }

    @Override
    public Listing unpublish(UUID id) {
        return this.saveUnpublish(this.getById(id));
    }
    
    private Listing save(Listing listing){
        return listingRepository.save(listing);
    }

    private Listing saveUnpublish(Listing listing){
        listing.unpublish();
        listing = this.save(listing);
        return listing;
    }

    public void changeTierLimit(int tierLimit){
        this.TIER_LIMIT = tierLimit;
    }
}
