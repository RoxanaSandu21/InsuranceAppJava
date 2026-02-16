package org.example.insuranceapp.infrastructure.persistence.repository;

import org.example.insuranceapp.infrastructure.persistence.entity.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCountryRepository extends JpaRepository<CountryEntity, Long> {
}
