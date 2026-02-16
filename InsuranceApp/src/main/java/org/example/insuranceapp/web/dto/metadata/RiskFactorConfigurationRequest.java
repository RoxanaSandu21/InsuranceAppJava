package org.example.insuranceapp.web.dto.metadata;

import jakarta.validation.constraints.NotNull;
import org.example.insuranceapp.domain.metadata.risk.RiskLevel;

import java.math.BigDecimal;

public record RiskFactorConfigurationRequest (
        @NotNull RiskLevel level,
        @NotNull Long referenceId,
        @NotNull BigDecimal adjustmentPercentage,
        boolean active
){
}
