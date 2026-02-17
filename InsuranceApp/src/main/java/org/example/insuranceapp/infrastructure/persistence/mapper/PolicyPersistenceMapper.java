package org.example.insuranceapp.infrastructure.persistence.mapper;

import org.example.insuranceapp.domain.policy.Policy;
import org.example.insuranceapp.infrastructure.persistence.entity.PolicyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BuildingPersistenceMapper.class})
public interface PolicyPersistenceMapper {
    PolicyEntity toEntity(Policy policy);
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "building.client", ignore = true)
    @Mapping(target = "building.policies", ignore = true)
    @Mapping(target = "building.city.buildings", ignore = true)
    @Mapping(target = "building.city.county.cities", ignore = true)
    @Mapping(target = "building.city.county.country.counties", ignore = true)
    Policy toDomain(PolicyEntity entity);
}
