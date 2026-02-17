package org.example.insuranceapp.infrastructure.persistence.mapper;

import org.example.insuranceapp.domain.geography.county.County;
import org.example.insuranceapp.infrastructure.persistence.entity.CountyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CityPersistenceMapper.class})
public interface CountyPersistenceMapper {
    CountyEntity toEntity(County county);
    @Mapping(target = "cities", ignore = true)
    @Mapping(target = "country.counties", ignore = true)
    County toDomain(CountyEntity entity);
}
