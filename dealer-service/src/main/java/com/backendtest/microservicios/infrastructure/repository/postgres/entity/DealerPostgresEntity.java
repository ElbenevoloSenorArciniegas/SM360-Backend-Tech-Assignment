package com.backendtest.microservicios.infrastructure.repository.postgres.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dealer")
@Data 
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DealerPostgresEntity implements Serializable{
    
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator( name = "UUID", strategy = "org.hibernate.id.UUIDGenerator" )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    private String name;
}
