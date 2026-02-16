package org.example.insuranceapp.web.dto.policy;

import org.example.insuranceapp.domain.policy.PolicyStatus;
import org.example.insuranceapp.web.dto.broker.BrokerResponse;
import org.example.insuranceapp.web.dto.building.BuildingResponse;
import org.example.insuranceapp.web.dto.client.ClientResponse;
import org.example.insuranceapp.web.dto.metadata.CurrencyResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record PolicyDetails (Long id, String policyNumber, ClientResponse client, BuildingResponse building, BrokerResponse broker, PolicyStatus policyStatus, LocalDate startDate, LocalDate endDate, BigDecimal basePremium, BigDecimal finalPremium, CurrencyResponse currency, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime cancellationDate){

}
