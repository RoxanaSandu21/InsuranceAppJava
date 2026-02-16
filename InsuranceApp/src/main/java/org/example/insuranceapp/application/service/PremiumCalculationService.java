package org.example.insuranceapp.application.service;

import org.example.insuranceapp.domain.policy.Policy;
import org.example.insuranceapp.infrastructure.premium.PremiumAdjustmentStrategy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class PremiumCalculationService {

    private final List<PremiumAdjustmentStrategy> strategies;

    public PremiumCalculationService(List<PremiumAdjustmentStrategy> strategies) {
        this.strategies = (strategies == null) ? List.of() : strategies;
    }

    public BigDecimal calculateFinalPremium(Policy policy) {
        BigDecimal basePremium = policy.getBasePremium();
        BigDecimal totalPercentageAdjustment = BigDecimal.ZERO;

        for (PremiumAdjustmentStrategy strategy : strategies) {
            BigDecimal adjustment = strategy.calculateAdjustment(policy);
            if (adjustment != null) {
                totalPercentageAdjustment = totalPercentageAdjustment.add(adjustment);
            }
        }

        BigDecimal multiplier = BigDecimal.ONE.add(
                totalPercentageAdjustment.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP)
        );

        return basePremium.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
    }
}

