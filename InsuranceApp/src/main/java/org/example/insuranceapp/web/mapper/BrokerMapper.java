package org.example.insuranceapp.web.mapper;

import org.example.insuranceapp.domain.broker.Broker;
import org.example.insuranceapp.web.dto.broker.BrokerRequest;
import org.example.insuranceapp.web.dto.broker.BrokerResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BrokerMapper {
    BrokerResponse toResponse(Broker broker);
    Broker toEntity(BrokerRequest brokerRequest);
}

