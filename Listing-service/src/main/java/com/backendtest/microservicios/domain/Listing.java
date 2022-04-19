package com.backendtest.microservicios.domain;

import java.io.Serializable;
import java.util.Date;
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
public class Listing implements Serializable{
    
    private UUID id;
    @NotBlank
    private String vehicle;
    @Positive
    private Long price;
    @NotBlank
    private Date createdAt;
    private State state;
    
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
