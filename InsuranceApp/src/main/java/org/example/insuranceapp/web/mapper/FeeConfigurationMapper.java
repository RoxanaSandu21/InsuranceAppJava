package org.example.insuranceapp.web.mapper;

import org.example.insuranceapp.domain.metadata.fee.FeeConfiguration;
import org.example.insuranceapp.web.dto.metadata.FeeConfigurationRequest;
import org.example.insuranceapp.web.dto.metadata.FeeConfigurationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FeeConfigurationMapper {
    @Mapping(source = "active", target = "active")
    FeeConfigurationResponse toResponse(FeeConfiguration feeConfiguration);
    @Mapping(source = "active", target = "active")
    @Mapping(target = "id", ignore = true)
    FeeConfiguration toEntity(FeeConfigurationRequest feeConfigurationRequest);
}
