package com.pragma.crecimiento.microservicios.imagen.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.pragma.crecimiento.microservicios.aplication.service.ImagenRepositoryInterface;
import com.pragma.crecimiento.microservicios.aplication.service.ImagenServiceImpl;
import com.pragma.crecimiento.microservicios.domain.Imagen;
import com.pragma.crecimiento.microservicios.domain.exception.ImagenNoEncontradaException;
import com.pragma.crecimiento.microservicios.domain.exception.ImagenYaRegistradaException;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ImagenServiceImplTest {
    
    @Mock
    private ImagenRepositoryInterface imagenRepository;

    @InjectMocks
    private ImagenServiceImpl imagenService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenRegistrar_withImagenNueva_thenRegistrar() {
        String dataImagen = "data";

        Imagen imagenARegistrar = Imagen.builder().data(dataImagen).build();
        Imagen imagenRegistrada = Imagen.builder().id("idAutogenerado").data(dataImagen).build();

        //Decimos que no existe actualmente una imagen con ese id
        Mockito.when(imagenRepository.findById(imagenARegistrar.getId()
        )).thenThrow(new ImagenNoEncontradaException("No existe una imagen con el id"));

        //Mockeamos el método save
        Mockito.when(imagenRepository.save(imagenARegistrar)).thenReturn(imagenRegistrada);

        //Y probamos que retorne lo mismo sin lanzar excepciones
        assertEquals(dataImagen, imagenService.registrar(imagenARegistrar).getData());
        //Y que traiga un id autogenerado
        assertNotNull(imagenService.registrar(imagenRegistrada).getId());
    }

    @Test
    void whenRegistrar_withImagenRegistrada_thenTrhowImagenYaRegistradaException() {
        String idImagen= "id";
        String dataImagen = "data";

        Imagen imagenARegistrar = Imagen.builder().id(idImagen).data(dataImagen).build();

        //Decimos que no existe actualmente una imagen con ese id
        Mockito.when(imagenRepository.findById(imagenARegistrar.getId()
        )).thenReturn(imagenARegistrar);

        //Y esperamos una excepción de ImagenYaRegistradaException
        
        Exception exception = assertThrows(ImagenYaRegistradaException.class, () -> {
            imagenService.registrar(imagenARegistrar);
        });
    
        String expectedMessage = "Ya existe una imagen registrada con el id";
        String actualMessage = exception.getMessage();
    
        assertTrue(actualMessage.contains(expectedMessage));

        //Nos aseguramos de que el mensaje tenga los campos que estamos pasando, para más semántica
        assertTrue(actualMessage.contains(imagenARegistrar.getId()));
    }

}
