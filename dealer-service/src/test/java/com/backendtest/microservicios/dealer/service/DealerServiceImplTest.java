package com.backendtest.microservicios.dealer.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.UUID;

import com.backendtest.microservicios.aplication.service.DealerRepositoryInterface;
import com.backendtest.microservicios.aplication.service.DealerServiceImpl;
import com.backendtest.microservicios.domain.Dealer;
import com.backendtest.microservicios.domain.exception.DealerNotFoundException;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DealerServiceImplTest {
    
    UUID idDealer = UUID.randomUUID();
    String nameDealer = "Jhon Dealer";
    Dealer registredDealer;
    Dealer requestDealer;

    @Mock
    private DealerRepositoryInterface dealerRepository;

    @InjectMocks
    private DealerServiceImpl dealerService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        this.requestDealer = Dealer.builder().name(this.nameDealer).build();

        this.registredDealer = Dealer.builder().id(this.idDealer).name(this.nameDealer).build();

        //Mock the save method
        Mockito.when(dealerRepository.save(this.requestDealer)).thenReturn(this.registredDealer);

        //Mock the dealer searching
        Mockito.when(dealerRepository.findById(this.idDealer)).thenReturn(Optional.of(this.registredDealer));
    }

    @Test
    void whenCreate_withNewDealer_thenCreate() {

        Dealer returnDealer = dealerService.create(this.requestDealer);
        //Testing the same name
        assertEquals(this.nameDealer, returnDealer.getName());
        
        //And gets an id
        assertNotNull(returnDealer.getId());
    }

    @Test
    void whenListById_withFoundDealer_thenReturn() {

        Dealer returnDealer = dealerService.getById(this.idDealer);
        //No exeption
        assertEquals(this.registredDealer, returnDealer);
    }

    @Test
    void whenListById_withNotFoundDealer_thenThrowDealerNotFoundException() {

        //Override the mock for the dealer searching
        Mockito.when(dealerRepository.findById(this.idDealer)).thenReturn(Optional.empty());

        //Wait for a ListingNotFoundException
        Exception exception = assertThrows(DealerNotFoundException.class, () -> {
            dealerService.getById(this.idDealer);
        });
    
        String expectedMessage = "Not found Dealer with id ";
        String actualMessage = exception.getMessage();
    
        assertTrue(actualMessage.contains(expectedMessage));

        //Got the id in the message for semantic
        assertTrue(actualMessage.contains(this.idDealer.toString()));
    }

}
