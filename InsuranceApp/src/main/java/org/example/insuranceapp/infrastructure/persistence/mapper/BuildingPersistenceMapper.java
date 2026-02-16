package org.example.insuranceapp.infrastructure.persistence.mapper;

import org.example.insuranceapp.domain.building.Building;
import org.example.insuranceapp.infrastructure.persistence.entity.BuildingEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BuildingPersistenceMapper {
    BuildingEntity toEntity(Building building);

    @Mapping(target = "client.buildings", ignore = true)
    @Mapping(target = "city.buildings", ignore = true)
    Building toDomain(BuildingEntity entity);
}
