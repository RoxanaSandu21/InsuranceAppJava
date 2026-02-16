package org.example.insuranceapp.web.dto.building;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.example.insuranceapp.domain.building.BuildingType;
import org.example.insuranceapp.domain.building.RiskIndicator;

import java.math.BigDecimal;

public record BuildingRequest(
        @NotBlank(message = "Street name cannot be empty.")
        String street,
        @Positive(message = "Street number must be valid.")
        int number,
        @NotNull(message = "City id cannot be empty.")
        Long cityId,
        @Positive(message = "Construction year must be valid.")
        int constructionYear,
        @NotNull(message = "Building type cannot be empty.")
        BuildingType type,
        @Positive(message = "Floor number must be valid.")
        int numberOfFloors,
        @Positive(message = "Surface area must be valid.")
        Long surfaceArea,
        @NotNull(message = "Insured value cannot be empty.") @DecimalMin(value = "0.01", message = "Insured value must be valid.")
        BigDecimal insuredValue,
        RiskIndicator riskIndicator) {
}
