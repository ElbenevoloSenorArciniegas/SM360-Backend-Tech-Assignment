package com.backendtest.microservicios.infrastructure.repository.postgres.mapper;

import com.backendtest.microservicios.domain.Listing;
import com.backendtest.microservicios.infrastructure.repository.postgres.entity.ListingPostgresEntity;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ListingPostgresMapper{

  private ModelMapper mapper = new ModelMapper();

  public Listing toDomain(ListingPostgresEntity listingEntity){
    return mapper.map(listingEntity, Listing.class);
  }

  public ListingPostgresEntity toEntity(Listing listing){
    return mapper.map(listing, ListingPostgresEntity.class);
  }

}
