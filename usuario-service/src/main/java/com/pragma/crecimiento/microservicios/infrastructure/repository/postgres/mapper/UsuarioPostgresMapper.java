package com.pragma.crecimiento.microservicios.infrastructure.repository.postgres.mapper;

import com.pragma.crecimiento.microservicios.domain.Usuario;
import com.pragma.crecimiento.microservicios.infrastructure.repository.postgres.entity.UsuarioPostgresEntity;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface UsuarioPostgresMapper {

  Usuario toDomain(UsuarioPostgresEntity usuarioEntity);

  UsuarioPostgresEntity toEntity(Usuario usuario);

}
