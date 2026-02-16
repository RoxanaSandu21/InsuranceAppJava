package org.example.insuranceapp.infrastructure.premium;

import org.example.insuranceapp.domain.policy.Policy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Order(3)
public class BrokerCommissionStrategy implements PremiumAdjustmentStrategy {
    @Override
    public BigDecimal calculateAdjustment(Policy policy) {
        return policy.getBroker().getCommissionPercentage() != null
                ? policy.getBroker().getCommissionPercentage()
                : BigDecimal.ZERO;
    }
}