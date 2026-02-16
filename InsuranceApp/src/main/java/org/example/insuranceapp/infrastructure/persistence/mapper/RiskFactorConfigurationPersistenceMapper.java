package org.example.insuranceapp.infrastructure.persistence.mapper;

import org.example.insuranceapp.domain.metadata.risk.RiskFactorConfiguration;
import org.example.insuranceapp.infrastructure.persistence.entity.RiskFactorConfigurationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RiskFactorConfigurationPersistenceMapper {
    RiskFactorConfigurationEntity toEntity(RiskFactorConfiguration riskFactorConfiguration);
    RiskFactorConfiguration toDomain(RiskFactorConfigurationEntity entity);
}
