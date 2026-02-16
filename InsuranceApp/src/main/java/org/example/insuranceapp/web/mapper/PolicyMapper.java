package org.example.insuranceapp.web.mapper;

import org.example.insuranceapp.domain.policy.Policy;
import org.example.insuranceapp.web.dto.policy.PolicyRequest;
import org.example.insuranceapp.web.dto.policy.PolicyResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PolicyMapper {

    @Mapping(target = "clientId", source = "client.id")
    @Mapping(target = "buildingId", source = "building.id")
    @Mapping(target = "brokerId", source = "broker.id")
    @Mapping(target = "currencyId", source = "currency.id")
    PolicyResponse toResponse(Policy policy);

    Policy toEntity(PolicyRequest policyRequest);
}
