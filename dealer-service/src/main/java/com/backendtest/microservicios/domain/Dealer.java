package com.backendtest.microservicios.domain;

import java.util.UUID;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dealer{
    
    private UUID id;

    @NotBlank
    private String name;

    public void removeId(){
        this.id = null;
    }
}
