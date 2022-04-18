package com.backendtest.microservicios.dealer.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import com.backendtest.microservicios.aplication.service.DealerRepositoryInterface;
import com.backendtest.microservicios.aplication.service.DealerServiceImpl;
import com.backendtest.microservicios.domain.Dealer;
import com.backendtest.microservicios.domain.exception.DealerNoEncontradoException;
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
        UUID idDealer= UUID.randomUUID();
        String nameDealer = "name";

        Dealer dealerARegistrar = Dealer.builder().name(nameDealer).build();
        Dealer dealerRegistrada = Dealer.builder().id(idDealer).name(nameDealer).build();

        //Decimos que no existe actualmente una dealer con ese id
        Mockito.when(dealerRepository.findById(dealerARegistrar.getId()
        )).thenThrow(new DealerNoEncontradoException("No existe un dealer con el id"));

        //Mockeamos el método save
        Mockito.when(dealerRepository.save(dealerARegistrar)).thenReturn(dealerRegistrada);

        //Y probamos que retorne lo mismo sin lanzar excepciones
        assertEquals(nameDealer, dealerService.registrar(dealerARegistrar).getName());
        //Y que traiga un id autogenerado
        assertNotNull(dealerService.registrar(dealerRegistrada).getId());
    }

    @Test
    void whenRegistrar_withDealerRegistrada_thenTrhowDealerYaRegistradaException() {
        UUID idDealer= UUID.randomUUID();
        String nameDealer = "name";

        Dealer dealerARegistrar = Dealer.builder().id(idDealer).name(nameDealer).build();

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
