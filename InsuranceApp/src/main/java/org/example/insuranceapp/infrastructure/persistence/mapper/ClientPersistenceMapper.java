package org.example.insuranceapp.infrastructure.persistence.mapper;

import org.example.insuranceapp.domain.client.Client;
import org.example.insuranceapp.infrastructure.persistence.entity.ClientEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {BuildingPersistenceMapper.class, PolicyPersistenceMapper.class})
public interface ClientPersistenceMapper {
    ClientEntity toEntity(Client client);
    Client toDomain(ClientEntity entity);
}
