package com.pragma.crecimiento.microservicios.usuario.controller;

import java.util.List;

import javax.validation.Valid;

import com.pragma.crecimiento.microservicios.usuario.entity.Usuario;
import com.pragma.crecimiento.microservicios.usuario.service.UsuarioServiceInterface;
import com.pragma.crecimiento.microservicios.util.ErrorMessagesFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value="/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioServiceInterface usuarioService;

    @GetMapping("/")
    public ResponseEntity<List<Usuario>> listarTodos(){
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.listarTodos());
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable(name = "idUsuario") Long idUsuario){
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.obtenerPorId(idUsuario));
    }

    @GetMapping("/documento/{tipo}/{numero}/")
    public ResponseEntity<Usuario> obtenerPorTipoIdentificacionNumeroIdentificacion(@PathVariable(name = "tipo") String tipo, @PathVariable(name = "numero") String numero){
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.obtenerPorTipoIdentificacionNumeroIdentificacion(tipo, numero));
    }

    @GetMapping("/edadMayorIgualA/{edadMinima}")
    public ResponseEntity<List<Usuario>> listarEdadMayorIgual(@PathVariable(name = "edadMinima") int edadMinima){
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.listarEdadMayorIgual(edadMinima));
    }

    @PostMapping("/")
    public ResponseEntity<Usuario> registrar(@Valid @RequestBody Usuario usuarioRequest, BindingResult result){
        if(result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessagesFormatter.formatearMensajesToString(result));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.registrar(usuarioRequest));
    }
    
    @PutMapping("/")
    public ResponseEntity<Usuario> actualizar(@Valid @RequestBody Usuario usuarioRequest, BindingResult result){
        if(result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessagesFormatter.formatearMensajesToString(result));
        }
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.actualizar(usuarioRequest));
    }

    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<Usuario> eliminar(@PathVariable(name = "idUsuario") Long idUsuario){
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.eliminar(idUsuario));
    }
}
