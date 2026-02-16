package org.example.insuranceapp.infrastructure.persistence.repository;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.example.insuranceapp.infrastructure.persistence.entity.CurrencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCurrencyRepository extends JpaRepository<CurrencyEntity, Long> {
    boolean existsByCode(@NotBlank @Size(min = 3, max =3) String code);
}
