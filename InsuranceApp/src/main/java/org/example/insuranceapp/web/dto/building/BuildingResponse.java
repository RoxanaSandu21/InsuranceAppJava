package org.example.insuranceapp.web.dto.building;

import org.example.insuranceapp.domain.building.BuildingType;
import org.example.insuranceapp.domain.building.RiskIndicator;
import org.example.insuranceapp.web.dto.geography.GeographyResponse;
import org.example.insuranceapp.web.dto.policy.PolicyResponse;

import java.math.BigDecimal;
import java.util.List;

public record BuildingResponse(
        Long id,
        Long clientId,
        String street,
        int number,
        int constructionYear,
        BuildingType type,
        int numberOfFloors,
        Long surfaceArea,
        BigDecimal insuredValue,
        RiskIndicator riskIndicator,
        GeographyResponse geography,
        List<PolicyResponse> policies
) {
}
