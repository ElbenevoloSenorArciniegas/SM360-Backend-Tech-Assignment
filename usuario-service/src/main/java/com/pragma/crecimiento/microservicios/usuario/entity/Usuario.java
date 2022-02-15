package com.pragma.crecimiento.microservicios.usuario.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import com.pragma.crecimiento.microservicios.usuario.model.Imagen;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data 
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nombre;
    private String apellido;
    @NotBlank(message = "El tipo de identificación no puede ser vacío")
    private String tipoIdentificacion;
    @NotBlank(message = "El número de identificación no puede ser vacío")
    private String numeroIdentificacion;
    @Positive(message = "La edad debe ser un número mayor que cero")
    private int edad;
    private String ciudadNacimiento;
    
    private String imagenId;
    @Transient
    private Imagen imagen;
}
