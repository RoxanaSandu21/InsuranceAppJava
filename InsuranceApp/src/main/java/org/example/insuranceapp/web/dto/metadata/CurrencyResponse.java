package org.example.insuranceapp.web.dto.metadata;

import java.math.BigDecimal;

public record CurrencyResponse(Long id, String code, String name, BigDecimal exchangeRateToBase, boolean active) {
}
