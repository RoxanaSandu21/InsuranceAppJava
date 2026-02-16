package org.example.insuranceapp.web.mapper;

import org.example.insuranceapp.domain.metadata.risk.RiskFactorConfiguration;
import org.example.insuranceapp.web.dto.metadata.RiskFactorConfigurationRequest;
import org.example.insuranceapp.web.dto.metadata.RiskFactorConfigurationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RiskFactorConfigurationMapper {
    @Mapping(source = "active", target = "active")
    RiskFactorConfigurationResponse toResponse(RiskFactorConfiguration riskFactorConfiguration);
    @Mapping(source = "active", target = "active")
    RiskFactorConfiguration toEntity(RiskFactorConfigurationRequest riskFactorConfigurationRequest);
}
