package org.example.insuranceapp.web.dto.metadata;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.example.insuranceapp.domain.metadata.fee.FeeType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FeeConfigurationRequest (
        @NotBlank String name,
        @NotNull FeeType type,
        @NotNull @PositiveOrZero BigDecimal percentage,
        LocalDate effectiveFrom,
        LocalDate effectiveTo,
        boolean active) {
}
