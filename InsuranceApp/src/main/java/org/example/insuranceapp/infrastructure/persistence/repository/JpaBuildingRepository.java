package org.example.insuranceapp.infrastructure.persistence.repository;

import org.example.insuranceapp.infrastructure.persistence.entity.BuildingEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaBuildingRepository extends JpaRepository<BuildingEntity, Long> {
    Page<BuildingEntity> findByClientId(Long clientId, Pageable pageable);
}
