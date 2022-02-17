package com.pragma.crecimiento.microservicios.aplication;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.pragma.crecimiento.microservicios.domain.Imagen;
import com.pragma.crecimiento.microservicios.domain.Usuario;
import com.pragma.crecimiento.microservicios.domain.exception.ImagenNoRegistradaException;
import com.pragma.crecimiento.microservicios.domain.exception.UsuarioNoEncontradoException;
import com.pragma.crecimiento.microservicios.domain.exception.UsuarioYaRegistradoException;
import com.pragma.crecimiento.microservicios.infrastructure.cliente.ImagenClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioServiceInterface{

    @Autowired
    private UsuarioRepositoryInterface usuarioRepository;

    @Autowired
    private ImagenClient imagenClient;

    @Override
    @Transactional
    public com.pragma.crecimiento.microservicios.domain.Usuario registrar(Usuario usuario) {

        try {
            obtenerPorTipoIdentificacionNumeroIdentificacion(usuario.getTipoIdentificacion(), usuario.getNumeroIdentificacion());
            throw new UsuarioYaRegistradoException("Ya existe un usuario registrado con el documento ["+usuario.getTipoIdentificacion() + "  " + usuario.getNumeroIdentificacion() + "]");
        } catch (UsuarioNoEncontradoException e) {
            //El usuario no existe, guardarlo
            Imagen imagenRegistrada = imagenClient.registrar(usuario.getImagen()).getBody();
            if(imagenRegistrada == null){
                throw new ImagenNoRegistradaException("La imagen asociada al usuario no pudo ser guardada");
            }
            usuario.setImagen(imagenRegistrada);
            return usuarioRepository.save(usuario);
        }

    }

    @Override
    public Usuario obtenerPorId(Long id){
        return buscarImagen(usuarioRepository.findById(id));
    }

    @Override
    public Usuario obtenerPorTipoIdentificacionNumeroIdentificacion(String tipoIdentificacion, String numeroIdentificacion){
        return buscarImagen(usuarioRepository.findByTipoIdentificacionAndNumeroIdentificacion(tipoIdentificacion, numeroIdentificacion));
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
        Usuario usuarioRegistrado = obtenerPorId(id);
        usuarioRepository.delete(usuarioRegistrado);
        return usuarioRegistrado;
    }

    private Usuario buscarImagen(Usuario usuario){
        if(usuario.hasEmptyImagen()){
            Imagen imagenReturn = imagenClient.obtenerPorId(usuario.getImagen().getId()).getBody();
            usuario.setImagen(imagenReturn);
        }
        return usuario;
    }
    
    
}