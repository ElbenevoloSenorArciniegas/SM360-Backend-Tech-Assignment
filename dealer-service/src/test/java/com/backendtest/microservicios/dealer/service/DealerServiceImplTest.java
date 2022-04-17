package com.backendtest.microservicios.dealer.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.backendtest.microservicios.aplication.service.DealerRepositoryInterface;
import com.backendtest.microservicios.aplication.service.DealerServiceImpl;
import com.backendtest.microservicios.domain.Dealer;
import com.backendtest.microservicios.domain.exception.DealerNoEncontradaException;
import com.backendtest.microservicios.domain.exception.DealerYaRegistradaException;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DealerServiceImplTest {
    
    @Mock
    private DealerRepositoryInterface dealerRepository;

    @InjectMocks
    private DealerServiceImpl dealerService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenRegistrar_withDealerNueva_thenRegistrar() {
        String dataDealer = "data";

        Dealer dealerARegistrar = Dealer.builder().data(dataDealer).build();
        Dealer dealerRegistrada = Dealer.builder().id(0L).data(dataDealer).build();

        //Decimos que no existe actualmente una dealer con ese id
        Mockito.when(dealerRepository.findById(dealerARegistrar.getId()
        )).thenThrow(new DealerNoEncontradaException("No existe una dealer con el id"));

        //Mockeamos el método save
        Mockito.when(dealerRepository.save(dealerARegistrar)).thenReturn(dealerRegistrada);

        //Y probamos que retorne lo mismo sin lanzar excepciones
        assertEquals(dataDealer, dealerService.registrar(dealerARegistrar).getData());
        //Y que traiga un id autogenerado
        assertNotNull(dealerService.registrar(dealerRegistrada).getId());
    }

    @Test
    void whenRegistrar_withDealerRegistrada_thenTrhowDealerYaRegistradaException() {
        Long idDealer= 0L;
        String dataDealer = "data";

        Dealer dealerARegistrar = Dealer.builder().id(idDealer).data(dataDealer).build();

        //Decimos que no existe actualmente una dealer con ese id
        Mockito.when(dealerRepository.findById(dealerARegistrar.getId()
        )).thenReturn(dealerARegistrar);

        //Y esperamos una excepción de DealerYaRegistradaException
        
        Exception exception = assertThrows(DealerYaRegistradaException.class, () -> {
            dealerService.registrar(dealerARegistrar);
        });
    
        String expectedMessage = "Ya existe una dealer registrada con el id";
        String actualMessage = exception.getMessage();
    
        assertTrue(actualMessage.contains(expectedMessage));

        //Nos aseguramos de que el mensaje tenga los campos que estamos pasando, para más semántica
        assertTrue(actualMessage.contains(dealerARegistrar.getId().toString()));
    }

}
