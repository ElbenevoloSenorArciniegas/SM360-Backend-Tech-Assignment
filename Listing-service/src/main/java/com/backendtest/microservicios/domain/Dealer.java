package com.backendtest.microservicios.domain;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dealer{
    
    private UUID id;
    private String name;

    private static final String DEFAULT_NAME = "none";

    public boolean isDefaultClientResponse(){
        return this.name.equals(DEFAULT_NAME);
    }

    public static Dealer getDefaultResponse(){
        return Dealer.builder().id(UUID.randomUUID()).name(DEFAULT_NAME).build();
    }
}
