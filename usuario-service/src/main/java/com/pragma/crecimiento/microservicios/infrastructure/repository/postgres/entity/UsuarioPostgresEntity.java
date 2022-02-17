package com.pragma.crecimiento.microservicios.infrastructure.repository.postgres.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data 
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioPostgresEntity implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nombre;
    private String apellido;
    private String tipoIdentificacion;
    private String numeroIdentificacion;
    private int edad;
    private String ciudadNacimiento;
    
    private String imagenId;
}
