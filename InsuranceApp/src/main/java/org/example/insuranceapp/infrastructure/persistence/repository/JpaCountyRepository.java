package org.example.insuranceapp.infrastructure.persistence.repository;

import org.example.insuranceapp.infrastructure.persistence.entity.CountyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCountyRepository extends JpaRepository<CountyEntity, Long> {
    Page<CountyEntity> findByCountryId(Long countryId, Pageable pageable);
}
