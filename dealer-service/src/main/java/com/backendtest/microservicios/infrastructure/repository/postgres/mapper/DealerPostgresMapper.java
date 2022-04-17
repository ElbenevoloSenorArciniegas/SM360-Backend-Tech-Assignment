package com.backendtest.microservicios.infrastructure.repository.postgres.mapper;

import org.springframework.stereotype.Component;

import com.backendtest.microservicios.domain.Dealer;
import com.backendtest.microservicios.infrastructure.repository.postgres.entity.DealerPostgresEntity;

import org.modelmapper.ModelMapper;

@Component
public class DealerPostgresMapper{

  private ModelMapper mapper = new ModelMapper();

  public Dealer toDomain(DealerPostgresEntity dealerEntity){
    return mapper.map(dealerEntity, Dealer.class);
  }

  public DealerPostgresEntity toEntity(Dealer dealer){
    return mapper.map(dealer, DealerPostgresEntity.class);
  }

}
