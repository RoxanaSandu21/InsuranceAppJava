package org.example.insuranceapp.web.dto.policy;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PolicyRequest (
        @NotNull Long clientId,
        @NotNull Long buildingId,
        @NotNull Long brokerId,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        @NotNull @Positive BigDecimal basePremium,
        @NotNull Long currencyId
){
}
