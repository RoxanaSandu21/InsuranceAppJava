package org.example.insuranceapp.infrastructure.persistence.repository;

import jakarta.validation.constraints.NotBlank;
import org.example.insuranceapp.infrastructure.persistence.entity.BrokerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaBrokerRepository extends JpaRepository<BrokerEntity, Long> {
    boolean existsByBrokerCode(@NotBlank String s);
}
