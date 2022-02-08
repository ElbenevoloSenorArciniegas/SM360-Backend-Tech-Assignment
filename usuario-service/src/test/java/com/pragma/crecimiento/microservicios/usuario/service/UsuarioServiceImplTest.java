package com.pragma.crecimiento.microservicios.usuario.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.pragma.crecimiento.microservicios.usuario.entity.Usuario;
import com.pragma.crecimiento.microservicios.usuario.exception.UsuarioNoEncontradoException;
import com.pragma.crecimiento.microservicios.usuario.exception.UsuarioYaRegistradoException;
import com.pragma.crecimiento.microservicios.usuario.repository.UsuarioRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenRegistrar_withUsuarioNuevo_thenRegistrar() {
        //Establecemos el usuario que llega por la petición
        Usuario usuarioARegistrar = Usuario.builder()
            .id(1L)
            .nombre("Pancho")
            .apellido("Villa")
            .edad(45)
            .ciudadNacimiento("Mexico")
            .tipoIdentificacion("CC")
            .numeroIdentificacion("1090521679")
            .build();

        //Decimos que no existe actualmente un usuario registrado con ese tipo y número de identificación
        Mockito.when(usuarioRepository.findByTipoIdentificacionAndNumeroIdentificacion(
            usuarioARegistrar.getTipoIdentificacion(), usuarioARegistrar.getNumeroIdentificacion()
        )).thenReturn(Optional.ofNullable(null));

        //Mockeamos el método save
        Mockito.when(usuarioRepository.save(usuarioARegistrar)).thenReturn(usuarioARegistrar);

        //Y probamos que retorne lo mismo sin lanzar excepciones
        assertEquals(1L, usuarioService.registrar(usuarioARegistrar).getId());
    }

    @Test
    void whenRegistrar_withUsuarioExistente_thenTrhowUsuarioYaRegistradoException() {

        //Establecemos que existe un usuario en BD
        Usuario usuarioRegistrado = Usuario.builder()
            .id(1L)
            .nombre("Juan")
            .apellido("Bautista")
            .edad(12)
            .ciudadNacimiento("Judá")
            .tipoIdentificacion("CC")
            .numeroIdentificacion("1090521679")
            .build();

        Mockito.when(usuarioRepository.findByTipoIdentificacionAndNumeroIdentificacion(
            usuarioRegistrado.getTipoIdentificacion(), usuarioRegistrado.getNumeroIdentificacion()
        )).thenReturn(Optional.of(usuarioRegistrado));

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
        Usuario usuarioPepe = Usuario.builder()
            .id(3L)
            .nombre("Pepe")
            .apellido("Pateatraseros")
            .edad(18)
            .ciudadNacimiento("Springfield")
            .tipoIdentificacion("CC")
            .numeroIdentificacion("1090521678")
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

        assertEquals(3, usuarioService.listarTodos().size());
    }

    @Test
    void whenActualizar_withUsuarioExistente_thenUsuarioModificado() {
        //Establecemos el usuario registrado en bd
        Usuario usuarioRegistrado = Usuario.builder()
            .id(1L)
            .nombre("Pancho")
            .apellido("Villa")
            .edad(45)
            .ciudadNacimiento("Mexico")
            .tipoIdentificacion("CC")
            .numeroIdentificacion("1090521679")
            .build();
        
        //Y el mock que lo consulta
        Mockito.when(usuarioRepository.findById(usuarioRegistrado.getId())).thenReturn(Optional.of(usuarioRegistrado));
        
        //Establecemos el usuario modificado de la petición
        Usuario usuarioModificado = Usuario.builder()
            .id(1L)
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
        //Establecemos el usuario modificado de la petición
        Usuario usuarioModificado = Usuario.builder()
            .id(1L)
            .nombre("Pancracio")
            .apellido("Nutriales")
            .edad(44)
            .ciudadNacimiento("Oslo")
            .tipoIdentificacion("CC")
            .numeroIdentificacion("1090521679")
            .build();
        
        //Establecemos que al buscar el usuario obtiene un null
        Mockito.when(usuarioRepository.findById(usuarioModificado.getId())).thenReturn(Optional.ofNullable(null));
        
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
        Mockito.when(usuarioRepository.findById(usuarioRegistrado.getId())).thenReturn(Optional.of(usuarioRegistrado));

        //El mock de delete por defecto no hace nada, lo dejams así

        usuarioRegistrado = usuarioService.eliminar(idUsuario);
        assertEquals(idUsuario, usuarioRegistrado.getId());
    }

    @Test
    void whenEliminar_withUsuarioNoExistente_thenThrowUsuarioNoEncontradoException() {
        Long idUsuario = 1L;

        //Establecemos que al buscar el usuario obtiene un null
        Mockito.when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.ofNullable(null));

        //El mock de delete por defecto no hace nada, lo dejams así

        //Y esperamos una excepción de UsuarioNoEncontradoException
        Exception exception = assertThrows(UsuarioNoEncontradoException.class, () -> {
            usuarioService.eliminar(1L);
        });
    
        //Nos aseguramos de que el mensaje tenga los campos que estamos pasando, para más semántica
        String expectedMessage = "No existe un usuario con el id";
        String actualMessage = exception.getMessage();
    
        assertTrue(actualMessage.contains(expectedMessage));
        assertTrue(actualMessage.contains(idUsuario.toString()));
    }

}
