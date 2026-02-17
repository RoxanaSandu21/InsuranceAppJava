package org.example.insuranceapp.infrastructure.persistence.mapper;

import org.example.insuranceapp.domain.client.Client;
import org.example.insuranceapp.infrastructure.persistence.entity.ClientEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BuildingPersistenceMapper.class, PolicyPersistenceMapper.class})
public interface ClientPersistenceMapper {
    ClientEntity toEntity(Client client);

    @Mapping(target = "buildings", ignore = true)
    @Mapping(target = "policies", ignore = true)
    Client toDomain(ClientEntity entity);
}
