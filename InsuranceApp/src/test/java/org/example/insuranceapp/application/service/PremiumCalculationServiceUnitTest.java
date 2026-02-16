package org.example.insuranceapp.application.service;

import org.example.insuranceapp.domain.broker.Broker;
import org.example.insuranceapp.domain.building.Building;
import org.example.insuranceapp.domain.building.BuildingType;
import org.example.insuranceapp.domain.building.RiskIndicator;
import org.example.insuranceapp.domain.geography.city.City;
import org.example.insuranceapp.domain.geography.country.Country;
import org.example.insuranceapp.domain.geography.county.County;
import org.example.insuranceapp.domain.policy.Policy;
import org.example.insuranceapp.infrastructure.premium.PremiumAdjustmentStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PremiumCalculationServiceUnitTest {

    @Mock
    PremiumAdjustmentStrategy s1;
    @Mock
    PremiumAdjustmentStrategy s2;
    @Mock
    PremiumAdjustmentStrategy s3;

    @InjectMocks
    private PremiumCalculationService calculationService;

    private Policy mockPolicy;
    private Building mockBuilding;
    private Broker mockBroker;

    @BeforeEach
    void setUp() {
        calculationService = new PremiumCalculationService(List.of(s1, s2, s3));

        mockBroker = new Broker();
        mockBroker.setCommissionPercentage(new BigDecimal("5.0"));

        mockBuilding = new Building();

        Country country = new Country("Romania");
        country.setId(1L);
        County county = new County("Cluj", country);
        county.setId(2L);
        City city = new City("Cluj-Napoca", county);
        city.setId(3L);

        mockBroker = new Broker();
        mockBroker.setCommissionPercentage(new BigDecimal("5.0"));

        mockBuilding = new Building();
        mockBuilding.setCity(city);
        mockBuilding.setType(BuildingType.RESIDENTIAL);
        mockBuilding.setRiskIndicator(RiskIndicator.EARTHQUAKE);

        mockPolicy = new Policy();
        mockPolicy.setBasePremium(new BigDecimal("1000.00"));
        mockPolicy.setBuilding(mockBuilding);
        mockPolicy.setBroker(mockBroker);
    }

    @Test
    void calculateFinalPremium_CompleteScenario() {
        when(s1.calculateAdjustment(mockPolicy)).thenReturn(new BigDecimal("2.0"));
        when(s2.calculateAdjustment(mockPolicy)).thenReturn(new BigDecimal("3.0"));
        when(s3.calculateAdjustment(mockPolicy)).thenReturn(new BigDecimal("6.0"));

        BigDecimal result = calculationService.calculateFinalPremium(mockPolicy);

        assertEquals(new BigDecimal("1110.00"), result);
    }

    @Test
    void calculateFinalPremium_IgnoresExpiredFees() {
        when(s1.calculateAdjustment(mockPolicy)).thenReturn(BigDecimal.ZERO);

        when(s2.calculateAdjustment(mockPolicy)).thenReturn(BigDecimal.ZERO);
        when(s3.calculateAdjustment(mockPolicy)).thenReturn(BigDecimal.ZERO);

        BigDecimal result = calculationService.calculateFinalPremium(mockPolicy);

        assertEquals(new BigDecimal("1000.00"), result);
    }


    @Test
    void calculateFinalPremium_NoAdjustments() {
        when(s1.calculateAdjustment(mockPolicy)).thenReturn(BigDecimal.ZERO);
        when(s2.calculateAdjustment(mockPolicy)).thenReturn(BigDecimal.ZERO);
        when(s3.calculateAdjustment(mockPolicy)).thenReturn(BigDecimal.ZERO);

        BigDecimal result = calculationService.calculateFinalPremium(mockPolicy);

        assertEquals(new BigDecimal("1000.00"), result);
    }

    @Test
    void calculateFinalPremium_NegativeAdjustments() {
        mockBroker.setCommissionPercentage(BigDecimal.ZERO);

        when(s1.calculateAdjustment(mockPolicy)).thenReturn(new BigDecimal("-10.0"));

        when(s2.calculateAdjustment(mockPolicy)).thenReturn(BigDecimal.ZERO);
        when(s3.calculateAdjustment(mockPolicy)).thenReturn(BigDecimal.ZERO);

        BigDecimal result = calculationService.calculateFinalPremium(mockPolicy);

        assertEquals(new BigDecimal("900.00"), result);
    }

}