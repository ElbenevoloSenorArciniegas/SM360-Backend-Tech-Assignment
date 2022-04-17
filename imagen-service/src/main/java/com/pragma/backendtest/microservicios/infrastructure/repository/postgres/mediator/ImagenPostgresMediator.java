package com.pragma.backendtest.microservicios.infrastructure.repository.postgres.mediator;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.pragma.backendtest.microservicios.aplication.service.ImagenRepositoryInterface;
import com.pragma.backendtest.microservicios.domain.Imagen;
import com.pragma.backendtest.microservicios.domain.exception.ImagenNoEncontradaException;
import com.pragma.backendtest.microservicios.infrastructure.repository.postgres.entity.ImagenPostgresEntity;
import com.pragma.backendtest.microservicios.infrastructure.repository.postgres.mapper.ImagenPostgresMapper;
import com.pragma.backendtest.microservicios.infrastructure.repository.postgres.repository.ImagenPostgresRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImagenPostgresMediator implements ImagenRepositoryInterface{

    Logger LOG = Logger.getLogger("ImagenPostgresMediator");

    @Autowired
    private ImagenPostgresRepository imagenRepository;

    @Autowired
    private ImagenPostgresMapper imagenMapper;

    @Override
    public Imagen save(Imagen imagen) {
        return imagenMapper.toDomain(imagenRepository.save(imagenMapper.toEntity(imagen)));
    }

    @Override
    public Imagen findById(Long id) {
        Optional<ImagenPostgresEntity> opcionalImagenReturn = imagenRepository.findById(id);
        if(opcionalImagenReturn.isPresent()){
            return imagenMapper.toDomain(opcionalImagenReturn.get());
        }
        throw new ImagenNoEncontradaException("No existe un imagen con el id "+id);
    }

    @Override
    public List<Imagen> findAll() {
        return imagenRepository.findAll().stream().map(
            imagenEntity -> imagenMapper.toDomain(imagenEntity)
        ).collect(Collectors.toList());
    }

    @Override
    public void delete(Imagen imagen) {
        imagenRepository.delete(imagenMapper.toEntity(imagen));        
    }
    
}
