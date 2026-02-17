package org.example.insuranceapp.domain.geography.country;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CountryRepository {
    Country save(Country country);
    Optional<Country> findById(Long id);
    boolean existsById(Long countryId);
    void deleteAll();
    Page<Country> findAll(Pageable pageable);
}
