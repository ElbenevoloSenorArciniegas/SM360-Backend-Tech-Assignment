package com.backendtest.microservicios.listing.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.backendtest.microservicios.aplication.ListingRepositoryInterface;
import com.backendtest.microservicios.aplication.ListingServiceImpl;
import com.backendtest.microservicios.domain.Dealer;
import com.backendtest.microservicios.domain.Listing;
import com.backendtest.microservicios.domain.exception.DealerNotFoundException;
import com.backendtest.microservicios.domain.exception.ListingNotFoundException;
import com.backendtest.microservicios.domain.exception.ListingYetRegistredException;
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

    @Mock
    private ListingRepositoryInterface listingRepository;

    @Mock
    private DealerClient dealerClient;

    @InjectMocks
    private ListingServiceImpl listingService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenRegistrar_withListingNuevo_thenRegistrar() {
        Long idListing = 1L;
        UUID idDealer = UUID.randomUUID();

        Dealer dealerARegistrar = Dealer.builder().id(idDealer).name("name").build();

        //Establecemos el listing que llega por la petición
        Listing listingARegistrar = Listing.builder()
            .id(idListing)
            .nombre("Pancho")
            .apellido("Villa")
            .edad(45)
            .ciudadNacimiento("Mexico")
            .tipoIdentificacion("CC")
            .numeroIdentificacion("1090521679")
            .dealer(dealerARegistrar)
            .build();

        //Decimos que no existe actualmente un listing registrado con ese tipo y número de identificación
        Mockito.when(listingRepository.findByTipoIdentificacionAndNumeroIdentificacion(
            listingARegistrar.getTipoIdentificacion(), listingARegistrar.getNumeroIdentificacion()
        )).thenThrow(new ListingNotFoundException("No existe un listing con el documento"));

        //Mockeamos el guardado exitoso de la dealer en el otro micro
        Mockito.when(dealerClient.registrar(dealerARegistrar)).thenReturn(ResponseEntity.ok(dealerARegistrar));

        //Mockeamos el método save
        Mockito.when(listingRepository.save(listingARegistrar)).thenReturn(listingARegistrar);

        //Y probamos que retorne lo mismo sin lanzar excepciones
        assertEquals(idListing, listingService.create(listingARegistrar).getId());

        //verify(listingRepository.save(listingARegistrar), times(1));
    }

    @Test
    void whenRegistrar_withListingExistente_thenTrhowListingYetRegistredException() {
        Long idListing = 1L;
        //Establecemos que existe un listing en BD
        Listing listingRegistrado = Listing.builder()
            .id(idListing)
            .nombre("Juan")
            .apellido("Bautista")
            .edad(12)
            .ciudadNacimiento("Judá")
            .tipoIdentificacion("CC")
            .numeroIdentificacion("1090521679")
            .build();

        Mockito.when(listingRepository.findByTipoIdentificacionAndNumeroIdentificacion(
            listingRegistrado.getTipoIdentificacion(), listingRegistrado.getNumeroIdentificacion()
        )).thenReturn(listingRegistrado);

        //Ahora establecemos el listing que llega por la petición. Comparten el mismo tipo y número de identificación
        
        Listing listingARegistrar = Listing.builder()
            .nombre("Pancho")
            .apellido("Villa")
            .edad(45)
            .ciudadNacimiento("Mexico")
            .tipoIdentificacion("CC")
            .numeroIdentificacion("1090521679")
            .build();
        
        Mockito.when(listingRepository.save(listingARegistrar)).thenReturn(listingARegistrar);

        //Y esperamos una excepción de ListingYetRegistredException
        
        Exception exception = assertThrows(ListingYetRegistredException.class, () -> {
            listingService.create(listingARegistrar);
        });
    
        String expectedMessage = "Ya existe un listing registrado con el documento";
        String actualMessage = exception.getMessage();
    
        assertTrue(actualMessage.contains(expectedMessage));

        //Nos aseguramos de que el mensaje tenga los campos que estamos pasando, para más semántica
        assertTrue(actualMessage.contains(listingARegistrar.getTipoIdentificacion()));
        assertTrue(actualMessage.contains(listingARegistrar.getNumeroIdentificacion()));
        
    }

    @Test
    void whenRegistrar_withDealerNoRegistrada_thenTrhowDealerNotRegistredException() {
        Long idListing = 1L;

        Dealer dealerARegistrar = null;

        //Establecemos el listing que llega por la petición
        Listing listingARegistrar = Listing.builder()
            .id(idListing)
            .nombre("Pancho")
            .apellido("Villa")
            .edad(45)
            .ciudadNacimiento("Mexico")
            .tipoIdentificacion("CC")
            .numeroIdentificacion("1090521679")
            .dealer(dealerARegistrar)
            .build();

        //Decimos que no existe actualmente un listing registrado con ese tipo y número de identificación
        Mockito.when(listingRepository.findByTipoIdentificacionAndNumeroIdentificacion(
            listingARegistrar.getTipoIdentificacion(), listingARegistrar.getNumeroIdentificacion()
        )).thenThrow(new ListingNotFoundException("No existe un listing con el documento"));

        //Mockeamos el guardado exitoso de la dealer en el otro micro
        Mockito.when(dealerClient.registrar(listingARegistrar.getDealer())).thenReturn(ResponseEntity.ok(dealerARegistrar));

        //Mockeamos el método save
        Mockito.when(listingRepository.save(listingARegistrar)).thenReturn(listingARegistrar);

        //Y esperamos una excepción de DealerNotRegistredException
        
        Exception exception = assertThrows(DealerNotFoundException.class, () -> {
            listingService.create(listingARegistrar);
        });
    
        String expectedMessage = "La dealer asociada al listing no pudo ser guardada";
        String actualMessage = exception.getMessage();
    
        assertTrue(actualMessage.contains(expectedMessage));        
    }

    @Test
    void whenListarEdadMayorA_thenReturnListings() {
        Listing listingPepe = Listing.builder()
            .nombre("Pepe")
            .edad(18)
            .build();
        
        Listing listingJuan = Listing.builder()
            .nombre("Juan")
            .edad(55)
            .build();

        final int EDAD_PRUEBA = 18;

        Mockito.when(listingRepository.findByEdadGreaterThanEqual(EDAD_PRUEBA)).thenReturn(Stream.of(listingPepe, listingJuan).collect(Collectors.toList()));

        List<Listing> listings = listingService.getAllByDealerAndState(EDAD_PRUEBA);
        assertEquals(2, listings.size());

        for (Listing listing : listings) {
            assertTrue(listing.getEdad() >= EDAD_PRUEBA);
        }
    }

    @Test
    void whenListarTodos_thenReturnListings(){
        Dealer dealerPepe = Dealer.builder().id(UUID.randomUUID()).build();
        Listing listingPepe = Listing.builder()
            .id(3L)
            .nombre("Pepe")
            .apellido("Pateatraseros")
            .edad(18)
            .ciudadNacimiento("Springfield")
            .tipoIdentificacion("CC")
            .numeroIdentificacion("1090521678")
            .dealer(dealerPepe)
            .build();
        
        Listing listingJuan = Listing.builder()
            .id(3L)
            .nombre("Juan")
            .apellido("Bautista")
            .edad(55)
            .ciudadNacimiento("Mexico")
            .tipoIdentificacion("CC")
            .numeroIdentificacion("1090521644")
            .build();

        Listing listingMenor = Listing.builder()
            .id(3L)
            .nombre("Juanito")
            .apellido("Bautista")
            .edad(17)
            .ciudadNacimiento("Mexico")
            .tipoIdentificacion("CC")
            .numeroIdentificacion("1090521644")
            .build();

        List<Listing> listings = new ArrayList<>();
        listings.add(listingPepe);
        listings.add(listingJuan);
        listings.add(listingMenor);

        Mockito.when(listingRepository.findAll()).thenReturn(Stream.of(listingPepe, listingJuan, listingMenor).collect(Collectors.toList()));

        //Mockeamos el dealerClient para buscar la dealer
        Mockito.when(dealerClient.obtenerPorId(listingPepe.getDealer().getId())).thenReturn(ResponseEntity.ok(dealerPepe));

        assertEquals(3, listingService.getAll().size());
    }

    @Test
    void whenActualizar_withListingExistente_withDealer_thenListingModificado() {
        Long idListing = 1L;

        //Establecemos el listing registrado en bd
        Listing listingRegistrado = Listing.builder()
            .id(idListing)
            .nombre("Pancho")
            .apellido("Villa")
            .edad(45)
            .ciudadNacimiento("Mexico")
            .tipoIdentificacion("CC")
            .numeroIdentificacion("1090521679")
            .build();
        
        //Y el mock que lo consulta
        Mockito.when(listingRepository.findById(listingRegistrado.getId())).thenReturn((listingRegistrado));
        
        Dealer dealerARegistrar = Dealer.builder().id(UUID.randomUUID()).name("name").build();
        //Establecemos el listing modificado de la petición
        Listing listingModificado = Listing.builder()
            .id(idListing)
            .nombre("Pancracio")
            .apellido("Nutriales")
            .edad(44)
            .ciudadNacimiento("Oslo")
            .tipoIdentificacion("CC")
            .numeroIdentificacion("1090521679")
            .dealer(dealerARegistrar)
            .build();

        //Y el mock que lo actualiza
        Mockito.when(listingRepository.save(listingModificado)).thenReturn(listingModificado);

        //Mockeamos el dealerClient para actualizar correctamente la dealer
        Mockito.when(dealerClient.actualizar(listingModificado.getDealer())).thenReturn(ResponseEntity.ok(dealerARegistrar));

        //Llamamos el método y esperamos que tengan el mismo id, no que se haya guardado uno nuevo con otro id...
        //También que no salte excepción de ListingNotFoundException
        listingModificado = listingService.update(listingModificado);
        assertEquals(listingRegistrado.getId(), listingModificado.getId());
    }

    @Test
    void whenActualizar_withListingExistente_withoutDealer_thenListingModificado() {
        Long idListing = 1L;
        //Establecemos el listing registrado en bd
        Listing listingRegistrado = Listing.builder()
            .id(idListing)
            .nombre("Pancho")
            .apellido("Villa")
            .edad(45)
            .ciudadNacimiento("Mexico")
            .tipoIdentificacion("CC")
            .numeroIdentificacion("1090521679")
            .build();
        
        //Y el mock que lo consulta
        Mockito.when(listingRepository.findById(listingRegistrado.getId())).thenReturn((listingRegistrado));
        
        //Establecemos el listing modificado de la petición
        Listing listingModificado = Listing.builder()
            .id(idListing)
            .nombre("Pancracio")
            .apellido("Nutriales")
            .edad(44)
            .ciudadNacimiento("Oslo")
            .tipoIdentificacion("CC")
            .numeroIdentificacion("1090521679")
            .build();
        
        //Y el mock que lo actualiza
        Mockito.when(listingRepository.save(listingModificado)).thenReturn(listingModificado);

        //Llamamos el método y esperamos que tengan el mismo id, no que se haya guardado uno nuevo con otro id...
        //También que no salte excepción de ListingNotFoundException
        listingModificado = listingService.update(listingModificado);
        assertEquals(listingRegistrado.getId(), listingModificado.getId());
    }
    
    @Test
    void whenActualizar_withListingNoExistente_thenThrowListingNotFoundException() {
        Long idListing = 1L;
        //Establecemos el listing modificado de la petición
        Listing listingModificado = Listing.builder()
            .id(idListing)
            .nombre("Pancracio")
            .apellido("Nutriales")
            .edad(44)
            .ciudadNacimiento("Oslo")
            .tipoIdentificacion("CC")
            .numeroIdentificacion("1090521679")
            .build();
        
        //Establecemos que al buscar el listing lanza una excepción
        Mockito.when(listingRepository.findById(listingModificado.getId())).thenThrow(new ListingNotFoundException("No existe un listing con el id "+idListing));
        
        //Y el mock que actualiza
        Mockito.when(listingRepository.save(listingModificado)).thenReturn(listingModificado);

        //Y esperamos una excepción de ListingNotFoundException
        Exception exception = assertThrows(ListingNotFoundException.class, () -> {
            listingService.update(listingModificado);
        });
    
        //Nos aseguramos de que el mensaje tenga los campos que estamos pasando, para más semántica
        String expectedMessage = "No existe un listing con el id";
        String actualMessage = exception.getMessage();
    
        assertTrue(actualMessage.contains(expectedMessage));
        assertTrue(actualMessage.contains(listingModificado.getId().toString()));
    }

    @Test
    void whenEliminar_withIdListingValido_thenListingEliminado() {
        Long idListing = 1L;

        //Establecemos el listing registrado en bd
        Listing listingRegistrado = Listing.builder()
            .id(idListing)
            .nombre("Pancho")
            .apellido("Villa")
            .edad(45)
            .ciudadNacimiento("Mexico")
            .tipoIdentificacion("CC")
            .numeroIdentificacion("1090521679")
            .build();

        //Establecemos el mock que busca el listing por id y lo encuentra
        Mockito.when(listingRepository.findById(listingRegistrado.getId())).thenReturn(listingRegistrado);

        //El mock de delete por defecto no hace nada, lo dejams así

        listingRegistrado = listingService.remove(idListing);
        assertEquals(idListing, listingRegistrado.getId());
    }

    @Test
    void whenEliminar_withIdListingValido_withDealer_thenListingEliminado() {
        Long idListing = 1L;

        Dealer dealerRegistrada = Dealer.builder().id(UUID.randomUUID()).name("name").build();
        //Establecemos el listing registrado en bd
        Listing listingRegistrado = Listing.builder()
            .id(idListing)
            .nombre("Pancho")
            .apellido("Villa")
            .edad(45)
            .ciudadNacimiento("Mexico")
            .tipoIdentificacion("CC")
            .numeroIdentificacion("1090521679")
            .dealer(dealerRegistrada)
            .build();

        //Establecemos el mock que busca el listing por id y lo encuentra
        Mockito.when(listingRepository.findById(listingRegistrado.getId())).thenReturn(listingRegistrado);

        //Mockeamos el dealerClient para eliminar la dealer
        Mockito.when(dealerClient.eliminar(listingRegistrado.getDealer().getId())).thenReturn(ResponseEntity.ok(dealerRegistrada));

        //El mock de delete por defecto no hace nada, lo dejams así

        listingRegistrado = listingService.remove(idListing);
        assertEquals(idListing, listingRegistrado.getId());
    }

    @Test
    void whenEliminar_withListingNoExistente_thenThrowListingNotFoundException() {
        Long idListing = 1L;

        //Establecemos que al buscar el listing lanza una excepción
        Mockito.when(listingRepository.findById(idListing)).thenThrow(new ListingNotFoundException("No existe un listing con el id "+idListing));

        //El mock de delete por defecto no hace nada, lo dejams así

        //Y esperamos una excepción de ListingNotFoundException
        Exception exception = assertThrows(ListingNotFoundException.class, () -> {
            listingService.remove(idListing);
        });
    
        //Nos aseguramos de que el mensaje tenga los campos que estamos pasando, para más semántica
        String expectedMessage = "No existe un listing con el id";
        String actualMessage = exception.getMessage();
    
        assertTrue(actualMessage.contains(expectedMessage));
        assertTrue(actualMessage.contains(idListing.toString()));
    }

}
