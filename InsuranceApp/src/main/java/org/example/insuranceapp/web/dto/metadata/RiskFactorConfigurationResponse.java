package org.example.insuranceapp.web.dto.metadata;

import org.example.insuranceapp.domain.metadata.risk.RiskLevel;

import java.math.BigDecimal;

public record RiskFactorConfigurationResponse (Long id, RiskLevel level, Long referenceId, BigDecimal adjustmentPercentage, boolean active){
}
