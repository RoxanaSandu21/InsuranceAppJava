package org.example.insuranceapp.infrastructure.persistence.repository;

import org.example.insuranceapp.infrastructure.persistence.entity.FeeConfigurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaFeeConfigurationRepository extends JpaRepository<FeeConfigurationEntity, Long> {
    List<FeeConfigurationEntity> findAllByActiveTrue();
}
