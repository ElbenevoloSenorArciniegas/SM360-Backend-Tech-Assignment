package com.backendtest.microservicios.infrastructure.repository.postgres.mediator;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.backendtest.microservicios.aplication.ListingRepositoryInterface;
import com.backendtest.microservicios.domain.Listing;
import com.backendtest.microservicios.domain.State;
import com.backendtest.microservicios.domain.exception.ListingNotFoundException;
import com.backendtest.microservicios.infrastructure.repository.postgres.entity.ListingPostgresEntity;
import com.backendtest.microservicios.infrastructure.repository.postgres.mapper.ListingPostgresMapper;
import com.backendtest.microservicios.infrastructure.repository.postgres.repository.ListingPostgresRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ListingPostgresMediator implements ListingRepositoryInterface{

    Logger LOG = Logger.getLogger("ListingPostgresMediator");

    @Autowired
    private ListingPostgresRepository listingRepository;

    @Autowired
    private ListingPostgresMapper listingMapper;

    @Override
    public Listing save(Listing listing) {
        ListingPostgresEntity listingEntity = listingMapper.toEntity(listing);
        if(listing.hasDealer()){
            listingEntity.setDealerId(listing.getDealer().getId());
        }
        Listing listingReturn = listingMapper.toDomain(listingRepository.save(listingEntity));
        listingReturn.setDealer(listing.getDealer()); //Returns full dealer object, not only its id
        return listingReturn;
    }

    @Override
    public Listing findById(UUID id) {
        Optional<ListingPostgresEntity> opcionalListingReturn = listingRepository.findById(id);
        if(opcionalListingReturn.isPresent()){
            return listingMapper.toDomain(opcionalListingReturn.get());
        }
        throw new ListingNotFoundException("No existe un listing con el id "+id);
    }

    @Override
    public List<Listing> findAll() {
        List<ListingPostgresEntity> listingEntityList = listingRepository.findAll();
        
        LOG.info("Listings: "+listingEntityList.size());
        
        return listingEntityList.stream().map(listingEntity -> {
            return listingMapper.toDomain(listingEntity); 
        }).collect(Collectors.toList());
    }

    @Override
    public void delete(Listing listing) {
        listingRepository.delete(listingMapper.toEntity(listing));
    }

    @Override
    public List<Listing> findByDealerAndStateOrderByCreatedAt(UUID idDealer, State state) {
        List<ListingPostgresEntity> listingEntityList = listingRepository.findByDealerAndStateOrderByCreatedAt(idDealer, state.toString());
        
        LOG.info("Listings: "+listingEntityList.size());

        return listingEntityList.stream().map(listingEntity -> {
            return listingMapper.toDomain(listingEntity); 
        }).collect(Collectors.toList());
    }
    
}
