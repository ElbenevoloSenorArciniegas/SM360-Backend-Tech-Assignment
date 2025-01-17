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
    private Double price;
    private Date createdAt;
    private State state;
    
    private Dealer dealer;

    public void setStateDefault(){
        this.state = State.DRAFT;
    }

    public void setCreatedAtNow(){
        this.createdAt = new Date();
    }

    public void publish(){
        this.state = State.PUBLISHED;
    }

    public void unpublish(){
        this.setStateDefault();
    }

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
