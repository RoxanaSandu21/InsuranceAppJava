package org.example.insuranceapp.web.mapper;

import org.example.insuranceapp.domain.building.Building;
import org.example.insuranceapp.domain.client.Client;
import org.example.insuranceapp.domain.policy.Policy;
import org.example.insuranceapp.infrastructure.persistence.entity.ClientEntity;
import org.example.insuranceapp.web.dto.client.ClientResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mapping(target = "buildingIds", expression = "java(mapBuildingsToIds(client.getBuildings()))")
    @Mapping(target = "policiesIds", expression = "java(mapPoliciesToIds(client.getPolicies()))")
    ClientResponse toResponse(Client client);

    default List<Long> mapBuildingsToIds(List<Building> buildings) {
        if (buildings == null) return List.of();
        return buildings.stream()
                .map(Building::getId)
                .toList();
    }

    default List<Long> mapPoliciesToIds(List<Policy> policies) {
        if (policies == null) return List.of();
        return policies.stream()
                .map(Policy::getId)
                .toList();
    }
}