package com.backendtest.microservicios.aplication;

import java.util.List;
import java.util.stream.Collectors;
import java.util.logging.Logger;

import javax.transaction.Transactional;

import com.backendtest.microservicios.domain.Dealer;
import com.backendtest.microservicios.domain.Listing;
import com.backendtest.microservicios.domain.exception.DealerNotRegistredException;
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
    public Listing registrar(Listing listing) {

        try {
            obtenerPorTipoIdentificacionNumeroIdentificacion(listing.getTipoIdentificacion(), listing.getNumeroIdentificacion());
            throw new ListingYetRegistredException("Ya existe un listing registrado con el documento ["+listing.getTipoIdentificacion() + "  " + listing.getNumeroIdentificacion() + "]");
        } catch (ListingNotFoundException e) {
            //El listing no existe, guardarlo
            Dealer dealerRegistrada = dealerClient.registrar(listing.getDealer()).getBody();
            if(dealerRegistrada == null){
                throw new DealerNotRegistredException("La dealer asociada al listing no pudo ser guardada");
            }
            LOG.info("dealer resgistrada: {"+dealerRegistrada.getId()+", "+dealerRegistrada.getName()+"}");
            listing.setDealer(dealerRegistrada);
            return listingRepository.save(listing);
        }

    }

    @Override
    public Listing obtenerPorId(Long id){
        return buscarDealer(listingRepository.findById(id));
    }

    @Override
    public Listing obtenerPorTipoIdentificacionNumeroIdentificacion(String tipoIdentificacion, String numeroIdentificacion){
        return buscarDealer(listingRepository.findByTipoIdentificacionAndNumeroIdentificacion(tipoIdentificacion, numeroIdentificacion));
    }

    @Override
    public List<Listing> listarTodos() {
        return listingRepository.findAll().stream().map(listing -> {
                return buscarDealer(listing); 
            }).collect(Collectors.toList());
    }

    @Override
    public List<Listing> listarEdadMayorIgual(int edad) {
        return listingRepository.findByEdadGreaterThanEqual(edad).stream().map(listing -> { 
            return buscarDealer(listing); 
        }).collect(Collectors.toList());
    }

    @Override
    public Listing actualizar(Listing listing){
        obtenerPorId(listing.getId()); //Revisa si existe, si no, lanza excepción
        if(listing.hasDealer()){
            dealerClient.actualizar(listing.getDealer());
        }
        return listingRepository.save(listing);
    }

    @Override
    @Transactional
    public Listing eliminar(Long id){
        Listing listingRegistrado = obtenerPorId(id);
        listingRepository.delete(listingRegistrado);
        if(listingRegistrado.hasDealer()){
            dealerClient.eliminar(listingRegistrado.getDealer().getId());
        }
        return listingRegistrado;
    }

    private Listing buscarDealer(Listing listing){
        if(listing.hasEmptyDealer()){
            Dealer dealerReturn = dealerClient.obtenerPorId(listing.getDealer().getId()).getBody();
            listing.setDealer(dealerReturn);
        }
        return listing;
    }
    
    
}
