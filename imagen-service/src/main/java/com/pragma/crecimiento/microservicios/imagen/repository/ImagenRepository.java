package com.pragma.crecimiento.microservicios.imagen.repository;

import com.pragma.crecimiento.microservicios.imagen.entity.Imagen;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagenRepository extends JpaRepository<Imagen, Long>{
    
}
