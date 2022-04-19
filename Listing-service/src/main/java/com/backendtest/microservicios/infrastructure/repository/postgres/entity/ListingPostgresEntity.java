package com.backendtest.microservicios.infrastructure.repository.postgres.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.backendtest.microservicios.domain.State;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "listing")
@Data 
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListingPostgresEntity implements Serializable{
    
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator( name = "UUID", strategy = "org.hibernate.id.UUIDGenerator" )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    private String vehicle;
    private Long price;
    private Date createdAt;
    @Enumerated(EnumType.STRING)
    private State state;
    
    private UUID dealerId;
}
