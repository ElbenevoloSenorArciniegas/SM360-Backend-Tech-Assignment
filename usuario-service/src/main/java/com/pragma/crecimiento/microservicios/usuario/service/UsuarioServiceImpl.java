package com.pragma.crecimiento.microservicios.usuario.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.pragma.crecimiento.microservicios.usuario.cliente.ImagenClient;
import com.pragma.crecimiento.microservicios.usuario.entity.Usuario;
import com.pragma.crecimiento.microservicios.usuario.exception.ImagenNoRegistradaException;
import com.pragma.crecimiento.microservicios.usuario.exception.UsuarioNoEncontradoException;
import com.pragma.crecimiento.microservicios.usuario.exception.UsuarioYaRegistradoException;
import com.pragma.crecimiento.microservicios.usuario.model.Imagen;
import com.pragma.crecimiento.microservicios.usuario.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioServiceInterface{

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ImagenClient imagenClient;

    @Override
    @Transactional
    public Usuario registrar(Usuario usuario) {

        try {
            obtenerPorTipoIdentificacionNumeroIdentificacion(usuario.getTipoIdentificacion(), usuario.getNumeroIdentificacion());
            throw new UsuarioYaRegistradoException("Ya existe un usuario registrado con el documento ["+usuario.getTipoIdentificacion() + "  " + usuario.getNumeroIdentificacion() + "]");
        } catch (UsuarioNoEncontradoException e) {
            //El usuario no existe, guardarlo
            Imagen imagenRegistrada = imagenClient.registrar(usuario.getImagen()).getBody();
            if(imagenRegistrada == null){
                throw new ImagenNoRegistradaException("La imagen asociada al usuario no pudo ser guardada");
            }
            usuario.setImagenId(imagenRegistrada.getId());
            usuario.setImagen(imagenRegistrada);
            return usuarioRepository.save(usuario);
        }

    }

    @Override
    public Usuario obtenerPorId(Long id){
        Optional<Usuario> opcionalUsuarioReturn = usuarioRepository.findById(id);
        if(opcionalUsuarioReturn.isPresent()){
            return buscarImagen(opcionalUsuarioReturn.get());
        }
        throw new UsuarioNoEncontradoException("No existe un usuario con el id "+id);
    }

    @Override
    public Usuario obtenerPorTipoIdentificacionNumeroIdentificacion(String tipoIdentificacion, String numeroIdentificacion){
        Optional<Usuario> opcionalUsuarioReturn = usuarioRepository.findByTipoIdentificacionAndNumeroIdentificacion(tipoIdentificacion, numeroIdentificacion);
        if(opcionalUsuarioReturn.isPresent()){
            return buscarImagen(opcionalUsuarioReturn.get());
        }
        throw new UsuarioNoEncontradoException("No existe un usuario con el documento ["+tipoIdentificacion + "  " + numeroIdentificacion + "]");
    }

    @Override
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll().stream().map(usuario -> { 
                return buscarImagen(usuario); 
            }).collect(Collectors.toList());
    }

    @Override
    public List<Usuario> listarEdadMayorIgual(int edad) {
        return usuarioRepository.findByEdadGreaterThanEqual(edad).stream().map(usuario -> { 
            return buscarImagen(usuario); 
        }).collect(Collectors.toList());
    }

    @Override
    public Usuario actualizar(Usuario usuario){
        obtenerPorId(usuario.getId()); //Revisa si existe, si no, lanza excepción
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public Usuario eliminar(Long id){
        Usuario usuarioBD = obtenerPorId(id);
        usuarioRepository.delete(usuarioBD);
        return usuarioBD;
    }

    private Usuario buscarImagen(Usuario usuario){
        if(usuario.getImagenId() != null){
            Imagen imagenReturn = imagenClient.obtenerPorId(usuario.getImagenId()).getBody();
            usuario.setImagen(imagenReturn);
        }
        return usuario;
    }
    
    
}
