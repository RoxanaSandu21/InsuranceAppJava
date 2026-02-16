package org.example.insuranceapp.web.dto.client;

import org.example.insuranceapp.domain.client.ClientType;

import java.util.List;

public record ClientResponse(
        Long id,
        ClientType type,
        String name,
        String identificationNumber,
        String email,
        String phoneNumber,
        String address,
        List<Long> buildingIds,
        List<Long> policiesIds
) {
}
