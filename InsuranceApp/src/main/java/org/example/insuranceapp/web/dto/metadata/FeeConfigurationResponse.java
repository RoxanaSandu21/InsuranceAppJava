package org.example.insuranceapp.web.dto.metadata;

import org.example.insuranceapp.domain.metadata.fee.FeeType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FeeConfigurationResponse (Long id, String name, FeeType type, BigDecimal percentage, LocalDate effectiveFrom, LocalDate effectiveTo, boolean active) {
}
