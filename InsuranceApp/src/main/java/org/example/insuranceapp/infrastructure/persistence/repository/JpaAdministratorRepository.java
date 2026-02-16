package org.example.insuranceapp.infrastructure.persistence.repository;

import org.example.insuranceapp.infrastructure.persistence.entity.AdministratorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAdministratorRepository extends JpaRepository<AdministratorEntity, Long> {
}
