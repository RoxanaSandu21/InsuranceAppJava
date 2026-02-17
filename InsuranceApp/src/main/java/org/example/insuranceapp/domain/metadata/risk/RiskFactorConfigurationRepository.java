package org.example.insuranceapp.domain.metadata.risk;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface RiskFactorConfigurationRepository {
    RiskFactorConfiguration save(RiskFactorConfiguration riskFactorConfiguration);
    Optional<RiskFactorConfiguration> findById(Long id);
    Page<RiskFactorConfiguration> findAll(Pageable pageable);

    List<RiskFactorConfiguration> findByLevelAndReferenceIdAndActiveTrue(RiskLevel riskLevel, Long id);
    boolean existsByLevelAndReferenceIdAndActiveTrue(RiskLevel level, Long aLong);
    int countByLevelAndReferenceIdAndActiveTrue(RiskLevel level, Long aLong);

    void deleteAll();
    int count();
}
