package com.pragma.crecimiento.microservicios.domain;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario implements Serializable{
    
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
    
    private Imagen imagen;

    public boolean hasImagen(){
        return this.imagen != null;
    }

    public boolean hasEmptyImagen(){
        return this.hasImagen() && this.imagen.getData() == null;
    }

    public void setImagenId(String idImagen){
        if(!hasImagen()){
            this.imagen = new Imagen();
        }
        this.imagen.setId(idImagen);
    }
}
