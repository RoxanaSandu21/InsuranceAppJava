package org.example.insuranceapp.web.dto.report;

import java.math.BigDecimal;

public record ReportDto(
        String groupingKey,
        String currencyCode,
        Long count,
        BigDecimal totalFinalPremium,
        BigDecimal totalFinalPremiumInBaseCurrency
) {}