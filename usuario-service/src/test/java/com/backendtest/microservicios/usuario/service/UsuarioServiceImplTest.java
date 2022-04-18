package com.backendtest.microservicios.usuario.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.backendtest.microservicios.aplication.UsuarioRepositoryInterface;
import com.backendtest.microservicios.aplication.UsuarioServiceImpl;
import com.backendtest.microservicios.domain.Dealer;
import com.backendtest.microservicios.domain.Usuario;
import com.backendtest.microservicios.domain.exception.DealerNoRegistradaException;
import com.backendtest.microservicios.domain.exception.UsuarioNoEncontradoException;
import com.backendtest.microservicios.domain.exception.UsuarioYaRegistradoException;
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
public class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepositoryInterface usuarioRepository;

    @Mock
    private DealerClient dealerClient;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenRegistrar_withUsuarioNuevo_thenRegistrar() {
        Long idUsuario = 1L;
        UUID idDealer = UUID.randomUUID();

        Dealer dealerARegistrar = Dealer.builder().id(idDealer).name("name").build();

        //Establecemos el usuario que llega por la petición
        Usuario usuarioARegistrar = Usuario.builder()
            .id(idUsuario)
            .nombre("Pancho")
            .apellido("Villa")
            .edad(45)
            .ciudadNacimiento("Mexico")
            .tipoIdentificacion("CC")
            .numeroIdentificacion("1090521679")
            .dealer(dealerARegistrar)
            .build();

        //Decimos que no existe actualmente un usuario registrado con ese tipo y número de identificación
        Mockito.when(usuarioRepository.findByTipoIdentificacionAndNumeroIdentificacion(
            usuarioARegistrar.getTipoIdentificacion(), usuarioARegistrar.getNumeroIdentificacion()
        )).thenThrow(new UsuarioNoEncontradoException("No existe un usuario con el documento"));

        //Mockeamos el guardado exitoso de la dealer en el otro micro
        Mockito.when(dealerClient.registrar(dealerARegistrar)).thenReturn(ResponseEntity.ok(dealerARegistrar));

        //Mockeamos el método save
        Mockito.when(usuarioRepository.save(usuarioARegistrar)).thenReturn(usuarioARegistrar);

        //Y probamos que retorne lo mismo sin lanzar excepciones
        assertEquals(idUsuario, usuarioService.registrar(usuarioARegistrar).getId());

        //verify(usuarioRepository.save(usuarioARegistrar), times(1));
    }

    @Test
    void whenRegistrar_withUsuarioExistente_thenTrhowUsuarioYaRegistradoException() {
        Long idUsuario = 1L;
        //Establecemos que existe un usuario en BD
        Usuario usuarioRegistrado = Usuario.builder()
            .id(idUsuario)
            .nombre("Juan")
            .apellido("Bautista")
            .edad(12)
            .ciudadNacimiento("Judá")
            .tipoIdentificacion("CC")
            .numeroIdentificacion("1090521679")
            .build();

        Mockito.when(usuarioRepository.findByTipoIdentificacionAndNumeroIdentificacion(
            usuarioRegistrado.getTipoIdentificacion(), usuarioRegistrado.getNumeroIdentificacion()
        )).thenReturn(usuarioRegistrado);

        //Ahora establecemos el usuario que llega por la petición. Comparten el mismo tipo y número de identificación
        
        Usuario usuarioARegistrar = Usuario.builder()
            .nombre("Pancho")
            .apellido("Villa")
            .edad(45)
            .ciudadNacimiento("Mexico")
            .tipoIdentificacion("CC")
            .numeroIdentificacion("1090521679")
            .build();
        
        Mockito.when(usuarioRepository.save(usuarioARegistrar)).thenReturn(usuarioARegistrar);

        //Y esperamos una excepción de UsuarioYaRegistradoException
        
        Exception exception = assertThrows(UsuarioYaRegistradoException.class, () -> {
            usuarioService.registrar(usuarioARegistrar);
        });
    
        String expectedMessage = "Ya existe un usuario registrado con el documento";
        String actualMessage = exception.getMessage();
    
        assertTrue(actualMessage.contains(expectedMessage));

        //Nos aseguramos de que el mensaje tenga los campos que estamos pasando, para más semántica
        assertTrue(actualMessage.contains(usuarioARegistrar.getTipoIdentificacion()));
        assertTrue(actualMessage.contains(usuarioARegistrar.getNumeroIdentificacion()));
        
    }

    @Test
    void whenRegistrar_withDealerNoRegistrada_thenTrhowDealerNoRegistradaException() {
        Long idUsuario = 1L;

        Dealer dealerARegistrar = null;

        //Establecemos el usuario que llega por la petición
        Usuario usuarioARegistrar = Usuario.builder()
            .id(idUsuario)
            .nombre("Pancho")
            .apellido("Villa")
            .edad(45)
            .ciudadNacimiento("Mexico")
            .tipoIdentificacion("CC")
            .numeroIdentificacion("1090521679")
            .dealer(dealerARegistrar)
            .build();

        //Decimos que no existe actualmente un usuario registrado con ese tipo y número de identificación
        Mockito.when(usuarioRepository.findByTipoIdentificacionAndNumeroIdentificacion(
            usuarioARegistrar.getTipoIdentificacion(), usuarioARegistrar.getNumeroIdentificacion()
        )).thenThrow(new UsuarioNoEncontradoException("No existe un usuario con el documento"));

        //Mockeamos el guardado exitoso de la dealer en el otro micro
        Mockito.when(dealerClient.registrar(usuarioARegistrar.getDealer())).thenReturn(ResponseEntity.ok(dealerARegistrar));

        //Mockeamos el método save
        Mockito.when(usuarioRepository.save(usuarioARegistrar)).thenReturn(usuarioARegistrar);

        //Y esperamos una excepción de DealerNoRegistradaException
        
        Exception exception = assertThrows(DealerNoRegistradaException.class, () -> {
            usuarioService.registrar(usuarioARegistrar);
        });
    
        String expectedMessage = "La dealer asociada al usuario no pudo ser guardada";
        String actualMessage = exception.getMessage();
    
        assertTrue(actualMessage.contains(expectedMessage));        
    }

    @Test
    void whenListarEdadMayorA_thenReturnUsuarios() {
        Usuario usuarioPepe = Usuario.builder()
            .nombre("Pepe")
            .edad(18)
            .build();
        
        Usuario usuarioJuan = Usuario.builder()
            .nombre("Juan")
            .edad(55)
            .build();

        final int EDAD_PRUEBA = 18;

        Mockito.when(usuarioRepository.findByEdadGreaterThanEqual(EDAD_PRUEBA)).thenReturn(Stream.of(usuarioPepe, usuarioJuan).collect(Collectors.toList()));

        List<Usuario> usuarios = usuarioService.listarEdadMayorIgual(EDAD_PRUEBA);
        assertEquals(2, usuarios.size());

        for (Usuario usuario : usuarios) {
            assertTrue(usuario.getEdad() >= EDAD_PRUEBA);
        }
    }

    @Test
    void whenListarTodos_thenReturnUsuarios(){
        Dealer dealerPepe = Dealer.builder().id(UUID.randomUUID()).build();
        Usuario usuarioPepe = Usuario.builder()
            .id(3L)
            .nombre("Pepe")
            .apellido("Pateatraseros")
            .edad(18)
            .ciudadNacimiento("Springfield")
            .tipoIdentificacion("CC")
            .numeroIdentificacion("1090521678")
            .dealer(dealerPepe)
            .build();
        
        Usuario usuarioJuan = Usuario.builder()
            .id(3L)
            .nombre("Juan")
            .apellido("Bautista")
            .edad(55)
            .ciudadNacimiento("Mexico")
            .tipoIdentificacion("CC")
            .numeroIdentificacion("1090521644")
            .build();

        Usuario usuarioMenor = Usuario.builder()
            .id(3L)
            .nombre("Juanito")
            .apellido("Bautista")
            .edad(17)
            .ciudadNacimiento("Mexico")
            .tipoIdentificacion("CC")
            .numeroIdentificacion("1090521644")
            .build();

        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(usuarioPepe);
        usuarios.add(usuarioJuan);
        usuarios.add(usuarioMenor);

        Mockito.when(usuarioRepository.findAll()).thenReturn(Stream.of(usuarioPepe, usuarioJuan, usuarioMenor).collect(Collectors.toList()));

        //Mockeamos el dealerClient para buscar la dealer
        Mockito.when(dealerClient.obtenerPorId(usuarioPepe.getDealer().getId())).thenReturn(ResponseEntity.ok(dealerPepe));

        assertEquals(3, usuarioService.listarTodos().size());
    }

    @Test
    void whenActualizar_withUsuarioExistente_withDealer_thenUsuarioModificado() {
        Long idUsuario = 1L;

        //Establecemos el usuario registrado en bd
        Usuario usuarioRegistrado = Usuario.builder()
            .id(idUsuario)
            .nombre("Pancho")
            .apellido("Villa")
            .edad(45)
            .ciudadNacimiento("Mexico")
            .tipoIdentificacion("CC")
            .numeroIdentificacion("1090521679")
            .build();
        
        //Y el mock que lo consulta
        Mockito.when(usuarioRepository.findById(usuarioRegistrado.getId())).thenReturn((usuarioRegistrado));
        
        Dealer dealerARegistrar = Dealer.builder().id(UUID.randomUUID()).name("name").build();
        //Establecemos el usuario modificado de la petición
        Usuario usuarioModificado = Usuario.builder()
            .id(idUsuario)
            .nombre("Pancracio")
            .apellido("Nutriales")
            .edad(44)
            .ciudadNacimiento("Oslo")
            .tipoIdentificacion("CC")
            .numeroIdentificacion("1090521679")
            .dealer(dealerARegistrar)
            .build();

        //Y el mock que lo actualiza
        Mockito.when(usuarioRepository.save(usuarioModificado)).thenReturn(usuarioModificado);

        //Mockeamos el dealerClient para actualizar correctamente la dealer
        Mockito.when(dealerClient.actualizar(usuarioModificado.getDealer())).thenReturn(ResponseEntity.ok(dealerARegistrar));

        //Llamamos el método y esperamos que tengan el mismo id, no que se haya guardado uno nuevo con otro id...
        //También que no salte excepción de UsuarioNoEncontradoException
        usuarioModificado = usuarioService.actualizar(usuarioModificado);
        assertEquals(usuarioRegistrado.getId(), usuarioModificado.getId());
    }

    @Test
    void whenActualizar_withUsuarioExistente_withoutDealer_thenUsuarioModificado() {
        Long idUsuario = 1L;
        //Establecemos el usuario registrado en bd
        Usuario usuarioRegistrado = Usuario.builder()
            .id(idUsuario)
            .nombre("Pancho")
            .apellido("Villa")
            .edad(45)
            .ciudadNacimiento("Mexico")
            .tipoIdentificacion("CC")
            .numeroIdentificacion("1090521679")
            .build();
        
        //Y el mock que lo consulta
        Mockito.when(usuarioRepository.findById(usuarioRegistrado.getId())).thenReturn((usuarioRegistrado));
        
        //Establecemos el usuario modificado de la petición
        Usuario usuarioModificado = Usuario.builder()
            .id(idUsuario)
            .nombre("Pancracio")
            .apellido("Nutriales")
            .edad(44)
            .ciudadNacimiento("Oslo")
            .tipoIdentificacion("CC")
            .numeroIdentificacion("1090521679")
            .build();
        
        //Y el mock que lo actualiza
        Mockito.when(usuarioRepository.save(usuarioModificado)).thenReturn(usuarioModificado);

        //Llamamos el método y esperamos que tengan el mismo id, no que se haya guardado uno nuevo con otro id...
        //También que no salte excepción de UsuarioNoEncontradoException
        usuarioModificado = usuarioService.actualizar(usuarioModificado);
        assertEquals(usuarioRegistrado.getId(), usuarioModificado.getId());
    }
    
    @Test
    void whenActualizar_withUsuarioNoExistente_thenThrowUsuarioNoEncontradoException() {
        Long idUsuario = 1L;
        //Establecemos el usuario modificado de la petición
        Usuario usuarioModificado = Usuario.builder()
            .id(idUsuario)
            .nombre("Pancracio")
            .apellido("Nutriales")
            .edad(44)
            .ciudadNacimiento("Oslo")
            .tipoIdentificacion("CC")
            .numeroIdentificacion("1090521679")
            .build();
        
        //Establecemos que al buscar el usuario lanza una excepción
        Mockito.when(usuarioRepository.findById(usuarioModificado.getId())).thenThrow(new UsuarioNoEncontradoException("No existe un usuario con el id "+idUsuario));
        
        //Y el mock que actualiza
        Mockito.when(usuarioRepository.save(usuarioModificado)).thenReturn(usuarioModificado);

        //Y esperamos una excepción de UsuarioNoEncontradoException
        Exception exception = assertThrows(UsuarioNoEncontradoException.class, () -> {
            usuarioService.actualizar(usuarioModificado);
        });
    
        //Nos aseguramos de que el mensaje tenga los campos que estamos pasando, para más semántica
        String expectedMessage = "No existe un usuario con el id";
        String actualMessage = exception.getMessage();
    
        assertTrue(actualMessage.contains(expectedMessage));
        assertTrue(actualMessage.contains(usuarioModificado.getId().toString()));
    }

    @Test
    void whenEliminar_withIdUsuarioValido_thenUsuarioEliminado() {
        Long idUsuario = 1L;

        //Establecemos el usuario registrado en bd
        Usuario usuarioRegistrado = Usuario.builder()
            .id(idUsuario)
            .nombre("Pancho")
            .apellido("Villa")
            .edad(45)
            .ciudadNacimiento("Mexico")
            .tipoIdentificacion("CC")
            .numeroIdentificacion("1090521679")
            .build();

        //Establecemos el mock que busca el usuario por id y lo encuentra
        Mockito.when(usuarioRepository.findById(usuarioRegistrado.getId())).thenReturn(usuarioRegistrado);

        //El mock de delete por defecto no hace nada, lo dejams así

        usuarioRegistrado = usuarioService.eliminar(idUsuario);
        assertEquals(idUsuario, usuarioRegistrado.getId());
    }

    @Test
    void whenEliminar_withIdUsuarioValido_withDealer_thenUsuarioEliminado() {
        Long idUsuario = 1L;

        Dealer dealerRegistrada = Dealer.builder().id(UUID.randomUUID()).name("name").build();
        //Establecemos el usuario registrado en bd
        Usuario usuarioRegistrado = Usuario.builder()
            .id(idUsuario)
            .nombre("Pancho")
            .apellido("Villa")
            .edad(45)
            .ciudadNacimiento("Mexico")
            .tipoIdentificacion("CC")
            .numeroIdentificacion("1090521679")
            .dealer(dealerRegistrada)
            .build();

        //Establecemos el mock que busca el usuario por id y lo encuentra
        Mockito.when(usuarioRepository.findById(usuarioRegistrado.getId())).thenReturn(usuarioRegistrado);

        //Mockeamos el dealerClient para eliminar la dealer
        Mockito.when(dealerClient.eliminar(usuarioRegistrado.getDealer().getId())).thenReturn(ResponseEntity.ok(dealerRegistrada));

        //El mock de delete por defecto no hace nada, lo dejams así

        usuarioRegistrado = usuarioService.eliminar(idUsuario);
        assertEquals(idUsuario, usuarioRegistrado.getId());
    }

    @Test
    void whenEliminar_withUsuarioNoExistente_thenThrowUsuarioNoEncontradoException() {
        Long idUsuario = 1L;

        //Establecemos que al buscar el usuario lanza una excepción
        Mockito.when(usuarioRepository.findById(idUsuario)).thenThrow(new UsuarioNoEncontradoException("No existe un usuario con el id "+idUsuario));

        //El mock de delete por defecto no hace nada, lo dejams así

        //Y esperamos una excepción de UsuarioNoEncontradoException
        Exception exception = assertThrows(UsuarioNoEncontradoException.class, () -> {
            usuarioService.eliminar(idUsuario);
        });
    
        //Nos aseguramos de que el mensaje tenga los campos que estamos pasando, para más semántica
        String expectedMessage = "No existe un usuario con el id";
        String actualMessage = exception.getMessage();
    
        assertTrue(actualMessage.contains(expectedMessage));
        assertTrue(actualMessage.contains(idUsuario.toString()));
    }

}
