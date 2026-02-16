package org.example.insuranceapp.infrastructure.persistence.mapper;

import org.example.insuranceapp.domain.broker.Broker;
import org.example.insuranceapp.infrastructure.persistence.entity.BrokerEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BrokerPersistenceMapper {
    BrokerEntity toEntity(Broker broker);
    Broker toDomain(BrokerEntity entity);
}
