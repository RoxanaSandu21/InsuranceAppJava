package org.example.insuranceapp.infrastructure.persistence;

import jakarta.validation.constraints.NotNull;
import org.example.insuranceapp.domain.metadata.risk.RiskFactorConfiguration;
import org.example.insuranceapp.domain.metadata.risk.RiskFactorConfigurationRepository;
import org.example.insuranceapp.domain.metadata.risk.RiskLevel;
import org.example.insuranceapp.infrastructure.persistence.entity.RiskFactorConfigurationEntity;
import org.example.insuranceapp.infrastructure.persistence.mapper.RiskFactorConfigurationPersistenceMapper;
import org.example.insuranceapp.infrastructure.persistence.repository.JpaRiskFactorConfigurationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RiskFactorConfigurationRepositoryAdapter implements RiskFactorConfigurationRepository {

    private final JpaRiskFactorConfigurationRepository jpaRepository;
    private final RiskFactorConfigurationPersistenceMapper mapper;

    public RiskFactorConfigurationRepositoryAdapter(JpaRiskFactorConfigurationRepository jpaRepository, RiskFactorConfigurationPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public RiskFactorConfiguration save(RiskFactorConfiguration RiskFactorConfiguration) {
        RiskFactorConfigurationEntity entity = mapper.toEntity(RiskFactorConfiguration);
        RiskFactorConfigurationEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<RiskFactorConfiguration> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Page<RiskFactorConfiguration> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public void deleteAll(){
        jpaRepository.deleteAll();
    }

    @Override
    public int count(){
        return Math.toIntExact(jpaRepository.count());
    }

    @Override
    public List<RiskFactorConfiguration> findByLevelAndReferenceIdAndActiveTrue(RiskLevel riskLevel, Long id) {
        return jpaRepository.findByLevelAndReferenceIdAndActiveTrue(riskLevel, id).stream().map(mapper::toDomain).toList();
    }

    @Override
    public boolean existsByLevelAndReferenceIdAndActiveTrue(RiskLevel level, Long aLong) {
        return jpaRepository.existsByLevelAndReferenceIdAndActiveTrue(level, aLong);
    }

    @Override
    public int countByLevelAndReferenceIdAndActiveTrue(RiskLevel level, Long aLong) {
        return jpaRepository.countByLevelAndReferenceIdAndActiveTrue(level, aLong);
    }
}