package org.example.insuranceapp.infrastructure.persistence.mapper;

import org.example.insuranceapp.domain.geography.country.Country;
import org.example.insuranceapp.infrastructure.persistence.entity.CountryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CountyPersistenceMapper.class})
public interface CountryPersistenceMapper {
    CountryEntity toEntity(Country country);
    Country toDomain(CountryEntity entity);
}
