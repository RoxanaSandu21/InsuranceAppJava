package org.example.insuranceapp.infrastructure.persistence.mapper;

import org.example.insuranceapp.domain.metadata.currency.Currency;
import org.example.insuranceapp.infrastructure.persistence.entity.CurrencyEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CurrencyPersistenceMapper {
    CurrencyEntity toEntity(Currency currency);
    Currency toDomain(CurrencyEntity entity);
}
