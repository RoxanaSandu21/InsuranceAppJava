package org.example.insuranceapp.infrastructure.premium;

import org.example.insuranceapp.domain.building.Building;
import org.example.insuranceapp.domain.metadata.fee.FeeConfiguration;
import org.example.insuranceapp.domain.metadata.fee.FeeConfigurationRepository;
import org.example.insuranceapp.domain.metadata.fee.FeeType;
import org.example.insuranceapp.domain.policy.Policy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@Order(1)
public class FeeConfigurationStrategy implements PremiumAdjustmentStrategy {
    private final FeeConfigurationRepository feeRepo;

    public FeeConfigurationStrategy(FeeConfigurationRepository feeRepo) {
        this.feeRepo = feeRepo;
    }

    @Override
    public BigDecimal calculateAdjustment(Policy policy) {
        BigDecimal adjustment = BigDecimal.ZERO;
        LocalDate now = LocalDate.now();
        Building building = policy.getBuilding();

        List<FeeConfiguration> activeFees = feeRepo.findAllByActiveTrue();
        for (FeeConfiguration fee : activeFees) {
            if (isWithinValidity(fee, now)) {
                if (fee.getType() == FeeType.ADMIN_FEE || fee.getType() == FeeType.BROKER_COMMISSION) {
                    adjustment = adjustment.add(nullSafe(fee.getPercentage()));
                }

                if (fee.getType() == FeeType.RISK_ADJUSTMENT &&
                        building.getRiskIndicator() != null &&
                        fee.getName().equalsIgnoreCase(building.getRiskIndicator().name())) {
                    adjustment = adjustment.add(nullSafe(fee.getPercentage()));
                }
            }
        }
        return adjustment;
    }

    private boolean isWithinValidity(FeeConfiguration fee, LocalDate date) {
        boolean afterStart = (fee.getEffectiveFrom() == null || !date.isBefore(fee.getEffectiveFrom()));
        boolean beforeEnd = (fee.getEffectiveTo() == null || !date.isAfter(fee.getEffectiveTo()));
        return afterStart && beforeEnd;
    }

    private BigDecimal nullSafe(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
