package org.example.insuranceapp.web.dto.policy;

import org.example.insuranceapp.domain.policy.PolicyStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record PolicyResponse (Long id, String policyNumber, Long clientId, Long buildingId, Long brokerId, PolicyStatus policyStatus, LocalDate startDate, LocalDate endDate, BigDecimal basePremium, BigDecimal finalPremium, Long currencyId, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime cancellationDate){
}
