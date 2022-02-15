package com.pragma.crecimiento.microservicios.imagen.service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import com.pragma.crecimiento.microservicios.imagen.entity.Imagen;
import com.pragma.crecimiento.microservicios.imagen.exception.ImagenNoEncontradaException;
import com.pragma.crecimiento.microservicios.imagen.repository.ImagenRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ImagenServiceImpl implements ImagenServiceInterface{

    Logger LOG = Logger.getLogger("ImagenServiceImpl");

    @Autowired
    private ImagenRepository imagenRepository;

    @Override
    @Transactional
    public Imagen registrar(Imagen imagen) {
        imagen.setId(null); //Evita que se guarden las imágenes con id 0
        return imagenRepository.save(imagen);
    }

    @Override
    public Imagen obtenerPorId(String id){
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
    public Imagen eliminar(String id){
        Imagen imagenBD = obtenerPorId(id); //Revisa si existe, si no, lanza excepción
        imagenRepository.delete(imagenBD);
        return imagenBD;
    }

    
    
}
