package com.pragma.crecimiento.microservicios.imagen.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.pragma.crecimiento.microservicios.imagen.entity.Imagen;
import com.pragma.crecimiento.microservicios.imagen.exception.ImagenNoEncontradaException;
import com.pragma.crecimiento.microservicios.imagen.repository.ImagenRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImagenServiceImpl implements ImagenServiceInterface{

    @Autowired
    private ImagenRepository imagenRepository;

    public ImagenServiceImpl(ImagenRepository imagenRepository) {
        this.imagenRepository = imagenRepository;
    }

    @Override
    @Transactional
    public Imagen registrar(Imagen imagen) {
        return imagenRepository.save(imagen);
    }

    @Override
    public Imagen obtenerPorId(Long id){
        Optional<Imagen> opcionalImagenReturn = imagenRepository.findById(id);
        if(opcionalImagenReturn.isPresent()){
            return opcionalImagenReturn.get();
        }
        throw new ImagenNoEncontradaException("No existe un imagen con el id "+id);
    }

    @Override
    public List<Imagen> listarTodos() {
        return imagenRepository.findAll();
    }

    @Override
    public Imagen actualizar(Imagen imagen){
        obtenerPorId(imagen.getId()); //Revisa si existe, si no, lanza excepción
        return imagenRepository.save(imagen);
    }

    @Override
    @Transactional
    public Imagen eliminar(Long id){
        Imagen imagenBD = obtenerPorId(id); //Revisa si existe, si no, lanza excepción
        imagenRepository.delete(imagenBD);
        return imagenBD;
    }

    
    
}
