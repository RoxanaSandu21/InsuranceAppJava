package org.example.insuranceapp.infrastructure.persistence.mapper;

import org.example.insuranceapp.domain.metadata.fee.FeeConfiguration;
import org.example.insuranceapp.infrastructure.persistence.entity.FeeConfigurationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FeeConfigurationPersistenceMapper {
    FeeConfigurationEntity toEntity(FeeConfiguration feeConfiguration);
    FeeConfiguration toDomain(FeeConfigurationEntity entity);
}
