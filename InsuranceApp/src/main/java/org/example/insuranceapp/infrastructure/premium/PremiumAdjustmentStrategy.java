package org.example.insuranceapp.infrastructure.premium;

import org.example.insuranceapp.domain.policy.Policy;

import java.math.BigDecimal;

public interface PremiumAdjustmentStrategy {
    BigDecimal calculateAdjustment(Policy policy);

}
