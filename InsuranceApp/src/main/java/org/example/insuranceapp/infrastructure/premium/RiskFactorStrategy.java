package org.example.insuranceapp.infrastructure.premium;

import org.example.insuranceapp.domain.building.Building;
import org.example.insuranceapp.domain.metadata.risk.RiskFactorConfiguration;
import org.example.insuranceapp.domain.metadata.risk.RiskFactorConfigurationRepository;
import org.example.insuranceapp.domain.metadata.risk.RiskLevel;
import org.example.insuranceapp.domain.policy.Policy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Order(2)
public class RiskFactorStrategy implements PremiumAdjustmentStrategy {
    private final RiskFactorConfigurationRepository riskRepo;

    public RiskFactorStrategy(RiskFactorConfigurationRepository riskRepo) {
        this.riskRepo = riskRepo;
    }

    @Override
    public BigDecimal calculateAdjustment(Policy policy) {
        BigDecimal adjustment = BigDecimal.ZERO;
        Building building = policy.getBuilding();

        adjustment = adjustment.add(sumRisk(RiskLevel.CITY, building.getCity().getId()));
        adjustment = adjustment.add(sumRisk(RiskLevel.COUNTY, building.getCity().getCounty().getId()));
        adjustment = adjustment.add(sumRisk(RiskLevel.COUNTRY, building.getCity().getCounty().getCountry().getId()));

        adjustment = adjustment.add(sumRisk(RiskLevel.BUILDING_TYPE, (long) building.getType().ordinal()));

        return adjustment;
    }

    private BigDecimal sumRisk(RiskLevel level, Long referenceId) {
        if (referenceId == null) return BigDecimal.ZERO;

        List<RiskFactorConfiguration> risks =
                riskRepo.findByLevelAndReferenceIdAndActiveTrue(level, referenceId);

        BigDecimal sum = BigDecimal.ZERO;
        for (RiskFactorConfiguration rf : risks) {
            sum = sum.add(nullSafe(rf.getAdjustmentPercentage()));
        }
        return sum;
    }
    private BigDecimal nullSafe(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

}
