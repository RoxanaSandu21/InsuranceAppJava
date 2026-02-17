package org.example.insuranceapp.web.mapper;

import org.example.insuranceapp.domain.policy.Policy;
import org.example.insuranceapp.web.dto.policy.PolicyDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ClientMapper.class, BuildingMapper.class, BrokerMapper.class, CurrencyMapper.class})
public interface PolicyDetailsMapper {
    @Mapping(target = "client", source = "client")
    @Mapping(target = "building", source = "building")
    @Mapping(target = "broker", source = "broker")
    @Mapping(target = "currency", source = "currency")
    @Mapping(source = "building.city.id", target = "building.geography.cityId")
    @Mapping(source = "building.city.name", target = "building.geography.city")
    @Mapping(source = "building.city.county.id", target = "building.geography.countyId")
    @Mapping(source = "building.city.county.name", target = "building.geography.county")
    @Mapping(source = "building.city.county.country.id", target = "building.geography.countryId")
    @Mapping(source = "building.city.county.country.name", target = "building.geography.country")
    PolicyDetails toResponse(Policy policy);
}