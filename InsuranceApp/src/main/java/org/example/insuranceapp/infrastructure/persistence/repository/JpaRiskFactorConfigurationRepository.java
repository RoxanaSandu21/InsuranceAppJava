package org.example.insuranceapp.infrastructure.persistence.repository;

import jakarta.validation.constraints.NotNull;
import org.example.insuranceapp.domain.metadata.risk.RiskLevel;
import org.example.insuranceapp.infrastructure.persistence.entity.RiskFactorConfigurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaRiskFactorConfigurationRepository extends JpaRepository<RiskFactorConfigurationEntity, Long> {
    List<RiskFactorConfigurationEntity> findByLevelAndReferenceIdAndActiveTrue(RiskLevel riskLevel, Long id);
    boolean existsByLevelAndReferenceIdAndActiveTrue(@NotNull RiskLevel level, @NotNull Long aLong);

    int countByLevelAndReferenceIdAndActiveTrue(@NotNull RiskLevel level, @NotNull Long aLong);
}
