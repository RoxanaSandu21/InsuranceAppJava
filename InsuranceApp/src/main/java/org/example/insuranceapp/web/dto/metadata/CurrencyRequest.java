package org.example.insuranceapp.web.dto.metadata;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CurrencyRequest(
        @NotBlank @Size(min = 3, max =3) String code,
        String name,
        @Positive BigDecimal exchangeRateToBase,
        boolean active) {
}
