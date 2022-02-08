package com.pragma.crecimiento.microservicios.usuario.repository;


import java.util.List;
import java.util.Optional;

import com.pragma.crecimiento.microservicios.usuario.entity.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    
    Optional<Usuario> findByTipoIdentificacionAndNumeroIdentificacion(String tipoIdentificacion, String numeroIdentificacion);

    List<Usuario> findByEdadGreaterThanEqual(int edad);

    
}
