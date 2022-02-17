package com.pragma.crecimiento.microservicios.aplication;

import java.util.List;

import com.pragma.crecimiento.microservicios.domain.Usuario;

public interface UsuarioServiceInterface {
    
    Usuario registrar(Usuario usuario);

    Usuario obtenerPorId(Long id);

    Usuario obtenerPorTipoIdentificacionNumeroIdentificacion(String tipoIdentificacion, String numeroIdentificacion);

    List<Usuario> listarTodos();

    List<Usuario> listarEdadMayorIgual(int edad);

    Usuario actualizar(Usuario usuario);

    Usuario eliminar(Long id);
}
