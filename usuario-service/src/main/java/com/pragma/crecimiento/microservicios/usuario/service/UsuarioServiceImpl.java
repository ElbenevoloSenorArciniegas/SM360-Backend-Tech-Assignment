package com.pragma.crecimiento.microservicios.usuario.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.pragma.crecimiento.microservicios.usuario.entity.Usuario;
import com.pragma.crecimiento.microservicios.usuario.exception.UsuarioNoEncontradoException;
import com.pragma.crecimiento.microservicios.usuario.exception.UsuarioYaRegistradoException;
import com.pragma.crecimiento.microservicios.usuario.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioServiceInterface{

    @Autowired
    private UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional
    public Usuario registrar(Usuario usuario) {

        try {
            obtenerPorTipoIdentificacionNumeroIdentificacion(usuario.getTipoIdentificacion(), usuario.getNumeroIdentificacion());
            throw new UsuarioYaRegistradoException("Ya existe un usuario registrado con el documento ["+usuario.getTipoIdentificacion() + "  " + usuario.getNumeroIdentificacion() + "]");
        } catch (UsuarioNoEncontradoException e) {
            //El usuario no existe, guardarlo
            return usuarioRepository.save(usuario);
        }

    }

    @Override
    public Usuario obtenerPorId(Long id){
        Optional<Usuario> opcionalUsuarioReturn = usuarioRepository.findById(id);
        if(opcionalUsuarioReturn.isPresent()){
            return opcionalUsuarioReturn.get();
        }
        throw new UsuarioNoEncontradoException("No existe un usuario con el id "+id);
    }

    @Override
    public Usuario obtenerPorTipoIdentificacionNumeroIdentificacion(String tipoIdentificacion, String numeroIdentificacion){
        Optional<Usuario> opcionalUsuarioReturn = usuarioRepository.findByTipoIdentificacionAndNumeroIdentificacion(tipoIdentificacion, numeroIdentificacion);
        if(opcionalUsuarioReturn.isPresent()){
            return opcionalUsuarioReturn.get();
        }
        throw new UsuarioNoEncontradoException("No existe un usuario con el documento ["+tipoIdentificacion + "  " + numeroIdentificacion + "]");
    }

    @Override
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    public List<Usuario> listarEdadMayorIgual(int edad) {
        return usuarioRepository.findByEdadGreaterThanEqual(edad);
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

    
    
}
