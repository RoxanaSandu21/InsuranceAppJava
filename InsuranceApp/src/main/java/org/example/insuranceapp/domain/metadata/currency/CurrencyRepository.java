package org.example.insuranceapp.domain.metadata.currency;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CurrencyRepository {
    Currency save(Currency currency);
    Optional<Currency> findById(Long id);
    Page<Currency> findAll(Pageable pageable);
    boolean existsByCode(String code);
}
