package com.backendtest.microservicios.domain;

import java.io.Serializable;
import java.util.UUID;

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
    
    private Dealer dealer;

    public boolean hasDealer(){
        return this.dealer != null;
    }

    public boolean hasEmptyDealer(){
        return this.hasDealer() && this.dealer.getName() == null;
    }

    public void setDealerId(UUID idDealer){
        if(!hasDealer()){
            this.dealer = new Dealer();
        }
        this.dealer.setId(idDealer);
    }
}
