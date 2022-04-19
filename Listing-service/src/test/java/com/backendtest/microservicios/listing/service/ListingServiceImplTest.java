package com.backendtest.microservicios.listing.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.backendtest.microservicios.aplication.ListingRepositoryInterface;
import com.backendtest.microservicios.aplication.ListingServiceImpl;
import com.backendtest.microservicios.domain.Dealer;
import com.backendtest.microservicios.domain.Listing;
import com.backendtest.microservicios.domain.PublicationMethod;
import com.backendtest.microservicios.domain.State;
import com.backendtest.microservicios.domain.exception.DealerNotFoundException;
import com.backendtest.microservicios.domain.exception.ListingNotFoundException;
import com.backendtest.microservicios.domain.exception.TierLimitException;
import com.backendtest.microservicios.infrastructure.cliente.DealerClient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class ListingServiceImplTest {

    UUID idDealer = UUID.randomUUID();
    Dealer registredDealer;
    
    UUID idListing = UUID.randomUUID();
    Listing listingARegistrar;

    List<Listing> listOfDealerAndPublished;

    @Mock
    private ListingRepositoryInterface listingRepository;

    @Mock
    private DealerClient dealerClient;

    @InjectMocks
    private ListingServiceImpl listingService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        this.registredDealer = Dealer.builder().id(this.idDealer).build();

        this.listingARegistrar = Listing.builder()
            .id(this.idListing)
            .vehicle("Some chevrolet")
            .price(50000D)
            .dealer(this.registredDealer)
            .build();

        //Mock the save
        Mockito.when(listingRepository.save(this.listingARegistrar)).thenReturn(this.listingARegistrar);

        //Mock the listing searching
        Mockito.when(listingRepository.findById(this.idListing)).thenReturn(this.listingARegistrar);

        //Mock the dealer searching
        Mockito.when(dealerClient.getById(this.idDealer)).thenReturn(ResponseEntity.ok(this.registredDealer));

        this.listOfDealerAndPublished = new ArrayList<>();
        this.listOfDealerAndPublished.add(
            Listing.builder()
                .vehicle("Some chevrolet")
                .price(50000D)
                .dealer(this.registredDealer)
                .state(State.PUBLISHED)
                .build()
        );
        this.listOfDealerAndPublished.add(
            Listing.builder()
                .vehicle("Some mazda")
                .price(45000D)
                .dealer(this.registredDealer)
                .state(State.PUBLISHED)
                .build()
        );

        //Mock the listing searching
        Mockito.when(listingRepository.findByDealerAndStateOrderByCreatedAt(this.idDealer, State.PUBLISHED)).thenReturn(this.listOfDealerAndPublished);
    }

    @Test
    void whenCreate_withNewListing_thenCreate() {

        Listing registredListing = listingService.create(this.listingARegistrar);

        //Getting the same id
        assertEquals(this.idListing, registredListing.getId());
        //Getting the default draft state
        assertEquals(State.DRAFT, registredListing.getState());

        LocalDate today = LocalDate.now();
        LocalDate createdAtDay = LocalDate.ofInstant(registredListing.getCreatedAt().toInstant(), ZoneId.systemDefault());
        //Created today
        assertTrue(today.isEqual(createdAtDay));
    }

    @Test
    void whenCreate_withNotDealer_thenTrhowsDealerNotFoundException() {

        //Override the mock for the dealersearching
        Mockito.when(dealerClient.getById(this.idDealer)).thenThrow(new DealerNotFoundException("Not found Dealer with id "+this.idDealer));

        //Wait for a DealerNotFoundException
        Exception exception = assertThrows(DealerNotFoundException.class, () -> {
            listingService.create(this.listingARegistrar);
        });
    
        String expectedMessage = "Not found Dealer with id ";
        String actualMessage = exception.getMessage();
    
        assertTrue(actualMessage.contains(expectedMessage));

        //Got the id in the message for semantic
        assertTrue(actualMessage.contains(this.idDealer.toString()));
    }

    @Test
    void whenUpdate_withFoundListing_thenUpdate() {

        //Mock the dealer searching
        Mockito.when(listingRepository.findById(this.idListing)).thenReturn(this.listingARegistrar);

        Listing updatedListing = listingService.update(this.listingARegistrar);

        LocalDate today = LocalDate.now();
        LocalDate createdAtDay = LocalDate.ofInstant(updatedListing.getCreatedAt().toInstant(), ZoneId.systemDefault());
        //Created today
        assertTrue(today.isEqual(createdAtDay));
    }

    @Test
    void whenUpdate_withNotFoundListing_thenTrhowsListingNotFoundException() {

        //Override the mock for the listing searching
        Mockito.when(listingRepository.findById(this.idListing)).thenReturn(null);

        //Wait for a ListingNotFoundException
        Exception exception = assertThrows(ListingNotFoundException.class, () -> {
            listingService.create(listingARegistrar);
        });
    
        String expectedMessage = "Not found Listing with id ";
        String actualMessage = exception.getMessage();
    
        assertTrue(actualMessage.contains(expectedMessage));

        //Got the id in the message for semantic
        assertTrue(actualMessage.contains(this.idListing.toString()));
    }

    @Test
    void whenPublish_withFoundListing_whitMethodTrhowsError_thenTrhowsTierLimitException() {

        //Wait for a TierLimitException
        Exception exception = assertThrows(TierLimitException.class, () -> {
            listingService.publish(this.idListing, PublicationMethod.THROWS_ERROR);
        });
    }

    @Test
    void whenPublish_withFoundListing_whitMethodReplaceLast_thenPublish() {

        Listing publishedListing = listingService.publish(this.idListing, PublicationMethod.REPLACE_LAST);

        assertEquals(State.PUBLISHED, publishedListing.getState());
    }

    @Test
    void whenUnpublish_withFoundListing_thenUnpublish() {

        Listing unpublishedListing = listingService.unpublish(this.idListing);

        assertEquals(State.DRAFT, unpublishedListing.getState());
    }

    @Test
    void whenGetAllByDealerAndStatedListing_withFoundDealer_withFoundState_thenGet() {

        List<Listing> list = listingService.getAllByDealerAndState(this.idDealer, State.PUBLISHED);
        assertIterableEquals(this.listOfDealerAndPublished, list);
    }

    @Test
    void whenGetAllByDealerAndStatedListing_withNotFoundDealer_withFoundState_thenEmpty() {

        //Override the mock for the listing searching
        Mockito.when(listingRepository.findByDealerAndStateOrderByCreatedAt(Mockito.any(UUID.class), State.PUBLISHED)).thenReturn(new ArrayList<>());

        List<Listing> list = listingService.getAllByDealerAndState(UUID.randomUUID(), State.PUBLISHED);
        assertTrue(list.isEmpty());
    }

    @Test
    void whenGetAllByDealerAndStatedListing_withFoundDealer_withNotFoundState_thenEmpty() {

        //Override the mock for the listing searching
        Mockito.when(listingRepository.findByDealerAndStateOrderByCreatedAt(this.idDealer, Mockito.any(State.class))).thenReturn(new ArrayList<>());

        List<Listing> list = listingService.getAllByDealerAndState(this.idDealer, State.PUBLISHED);
        assertTrue(list.isEmpty());
    }

}
