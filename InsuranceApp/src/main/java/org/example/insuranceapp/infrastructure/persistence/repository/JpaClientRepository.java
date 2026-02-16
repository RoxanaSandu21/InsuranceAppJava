package org.example.insuranceapp.infrastructure.persistence.repository;

import org.example.insuranceapp.infrastructure.persistence.entity.ClientEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaClientRepository extends JpaRepository<ClientEntity, Long> {
    boolean existsByIdentificationNumber(String identificationNumber);
    Optional<ClientEntity> findByIdentificationNumber(String identificationNumber);
    Page<ClientEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);
}