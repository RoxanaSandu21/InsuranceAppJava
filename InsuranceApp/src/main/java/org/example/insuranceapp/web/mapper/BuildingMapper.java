package org.example.insuranceapp.web.mapper;

import org.example.insuranceapp.domain.building.Building;
import org.example.insuranceapp.web.dto.building.BuildingResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PolicyMapper.class})
public interface BuildingMapper {
    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "city.id", target = "geography.cityId")
    @Mapping(source = "city.name", target = "geography.city")
    @Mapping(source = "city.county.id", target = "geography.countyId")
    @Mapping(source = "city.county.name", target = "geography.county")
    @Mapping(source = "city.county.country.id", target = "geography.countryId")
    @Mapping(source = "city.county.country.name", target = "geography.country")
    @Mapping(source = "policies", target = "policies")
    BuildingResponse toResponse(Building building);
}
