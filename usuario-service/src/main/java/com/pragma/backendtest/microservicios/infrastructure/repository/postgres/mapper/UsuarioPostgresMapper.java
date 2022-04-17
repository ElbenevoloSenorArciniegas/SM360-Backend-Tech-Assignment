package com.pragma.backendtest.microservicios.infrastructure.repository.postgres.mapper;

import com.pragma.backendtest.microservicios.domain.Usuario;
import com.pragma.backendtest.microservicios.infrastructure.repository.postgres.entity.UsuarioPostgresEntity;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UsuarioPostgresMapper{

  private ModelMapper mapper = new ModelMapper();

  public Usuario toDomain(UsuarioPostgresEntity usuarioEntity){
    return mapper.map(usuarioEntity, Usuario.class);
  }

  public UsuarioPostgresEntity toEntity(Usuario usuario){
    return mapper.map(usuario, UsuarioPostgresEntity.class);
  }

}
