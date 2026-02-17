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

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "policyNumber", ignore = true)
    @Mapping(target = "policyStatus", ignore = true)
    @Mapping(target = "finalPremium", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "cancellationDate", ignore = true)
    @Mapping(target = "cancellationReason", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "building", ignore = true)
    @Mapping(target = "broker", ignore = true)
    @Mapping(target = "currency", ignore = true)
    Policy toEntity(PolicyRequest policyRequest);
}
