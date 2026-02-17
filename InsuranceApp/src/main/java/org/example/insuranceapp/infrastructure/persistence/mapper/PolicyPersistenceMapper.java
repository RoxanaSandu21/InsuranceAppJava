package org.example.insuranceapp.infrastructure.persistence.mapper;

import org.example.insuranceapp.domain.policy.Policy;
import org.example.insuranceapp.infrastructure.persistence.entity.PolicyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PolicyPersistenceMapper {
    PolicyEntity toEntity(Policy policy);
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "building.policies", ignore = true)
    Policy toDomain(PolicyEntity entity);
}
