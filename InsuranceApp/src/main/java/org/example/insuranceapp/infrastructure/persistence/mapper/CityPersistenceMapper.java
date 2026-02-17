package org.example.insuranceapp.infrastructure.persistence.mapper;

import org.example.insuranceapp.domain.geography.city.City;
import org.example.insuranceapp.infrastructure.persistence.entity.CityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CityPersistenceMapper {
    CityEntity toEntity(City city);
    @Mapping(target = "county", ignore = true)
    City toDomain(CityEntity entity);
}
