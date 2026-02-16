package org.example.insuranceapp.infrastructure.persistence;

import org.example.insuranceapp.domain.metadata.fee.FeeConfiguration;
import org.example.insuranceapp.domain.metadata.fee.FeeConfigurationRepository;
import org.example.insuranceapp.infrastructure.persistence.entity.FeeConfigurationEntity;
import org.example.insuranceapp.infrastructure.persistence.mapper.FeeConfigurationPersistenceMapper;
import org.example.insuranceapp.infrastructure.persistence.repository.JpaFeeConfigurationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FeeConfigurationRepositoryAdapter implements FeeConfigurationRepository {

    private final JpaFeeConfigurationRepository jpaRepository;
    private final FeeConfigurationPersistenceMapper mapper;

    public FeeConfigurationRepositoryAdapter(JpaFeeConfigurationRepository jpaRepository, FeeConfigurationPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public FeeConfiguration save(FeeConfiguration FeeConfiguration) {
        FeeConfigurationEntity entity = mapper.toEntity(FeeConfiguration);
        FeeConfigurationEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<FeeConfiguration> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Page<FeeConfiguration> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public List<FeeConfiguration> findAllByActiveTrue() {
        return jpaRepository.findAllByActiveTrue().stream().map(mapper::toDomain).toList();
    }
}