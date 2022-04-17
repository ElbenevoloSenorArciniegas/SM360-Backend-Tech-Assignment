package com.backendtest.microservicios.aplication;

import java.util.List;
import java.util.stream.Collectors;
import java.util.logging.Logger;

import javax.transaction.Transactional;

import com.backendtest.microservicios.domain.Dealer;
import com.backendtest.microservicios.domain.Usuario;
import com.backendtest.microservicios.domain.exception.DealerNoRegistradaException;
import com.backendtest.microservicios.domain.exception.UsuarioNoEncontradoException;
import com.backendtest.microservicios.domain.exception.UsuarioYaRegistradoException;
import com.backendtest.microservicios.infrastructure.cliente.DealerClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioServiceInterface{

    Logger LOG = Logger.getLogger("UsuarioServiceImpl");

    @Autowired
    private UsuarioRepositoryInterface usuarioRepository;

    @Autowired
    private DealerClient dealerClient;

    @Override
    @Transactional
    public Usuario registrar(Usuario usuario) {

        try {
            obtenerPorTipoIdentificacionNumeroIdentificacion(usuario.getTipoIdentificacion(), usuario.getNumeroIdentificacion());
            throw new UsuarioYaRegistradoException("Ya existe un usuario registrado con el documento ["+usuario.getTipoIdentificacion() + "  " + usuario.getNumeroIdentificacion() + "]");
        } catch (UsuarioNoEncontradoException e) {
            //El usuario no existe, guardarlo
            Dealer dealerRegistrada = dealerClient.registrar(usuario.getDealer()).getBody();
            if(dealerRegistrada == null){
                throw new DealerNoRegistradaException("La dealer asociada al usuario no pudo ser guardada");
            }
            LOG.info("dealer resgistrada: {"+dealerRegistrada.getId()+", "+dealerRegistrada.getData()+"}");
            usuario.setDealer(dealerRegistrada);
            return usuarioRepository.save(usuario);
        }

    }

    @Override
    public Usuario obtenerPorId(Long id){
        return buscarDealer(usuarioRepository.findById(id));
    }

    @Override
    public Usuario obtenerPorTipoIdentificacionNumeroIdentificacion(String tipoIdentificacion, String numeroIdentificacion){
        return buscarDealer(usuarioRepository.findByTipoIdentificacionAndNumeroIdentificacion(tipoIdentificacion, numeroIdentificacion));
    }

    @Override
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll().stream().map(usuario -> {
                return buscarDealer(usuario); 
            }).collect(Collectors.toList());
    }

    @Override
    public List<Usuario> listarEdadMayorIgual(int edad) {
        return usuarioRepository.findByEdadGreaterThanEqual(edad).stream().map(usuario -> { 
            return buscarDealer(usuario); 
        }).collect(Collectors.toList());
    }

    @Override
    public Usuario actualizar(Usuario usuario){
        obtenerPorId(usuario.getId()); //Revisa si existe, si no, lanza excepción
        if(usuario.hasDealer()){
            dealerClient.actualizar(usuario.getDealer());
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public Usuario eliminar(Long id){
        Usuario usuarioRegistrado = obtenerPorId(id);
        usuarioRepository.delete(usuarioRegistrado);
        if(usuarioRegistrado.hasDealer()){
            dealerClient.eliminar(usuarioRegistrado.getDealer().getId());
        }
        return usuarioRegistrado;
    }

    private Usuario buscarDealer(Usuario usuario){
        if(usuario.hasEmptyDealer()){
            Dealer dealerReturn = dealerClient.obtenerPorId(usuario.getDealer().getId()).getBody();
            usuario.setDealer(dealerReturn);
        }
        return usuario;
    }
    
    
}
