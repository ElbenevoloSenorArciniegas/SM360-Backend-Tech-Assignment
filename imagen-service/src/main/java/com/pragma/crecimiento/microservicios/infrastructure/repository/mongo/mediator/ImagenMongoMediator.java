package com.pragma.crecimiento.microservicios.infrastructure.repository.mongo.mediator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.pragma.crecimiento.microservicios.aplication.service.ImagenRepositoryInterface;
import com.pragma.crecimiento.microservicios.domain.Imagen;
import com.pragma.crecimiento.microservicios.domain.exception.ImagenNoEncontradaException;
import com.pragma.crecimiento.microservicios.infrastructure.repository.mongo.entity.ImagenMongoEntity;
import com.pragma.crecimiento.microservicios.infrastructure.repository.mongo.mapper.ImagenMongoMapper;
import com.pragma.crecimiento.microservicios.infrastructure.repository.mongo.repository.ImagenMongoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImagenMongoMediator implements ImagenRepositoryInterface{

    @Autowired
    private ImagenMongoRepository imagenRepository;

    @Autowired
    private ImagenMongoMapper imagenMapper;

    @Override
    public Imagen save(Imagen imagen) {
        imagen.setId(null); //Evita que se guarden las imágenes con id 0. Obliga a autogenerar un id
        return imagenMapper.toDomain(imagenRepository.save(imagenMapper.toEntity(imagen)));
    }

    @Override
    public Imagen findById(String id) {
        Optional<ImagenMongoEntity> opcionalImagenReturn = imagenRepository.findById(id);
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
