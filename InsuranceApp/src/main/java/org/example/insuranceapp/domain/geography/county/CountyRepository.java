package org.example.insuranceapp.domain.geography.county;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CountyRepository {
    County save(County county);
    Optional<County> findById(Long id);
    Page<County> findAll(Pageable pageable);
    boolean existsById(Long countyId);
    void deleteAll();

    Page<County> findByCountryId(Long countryId, Pageable pageable);
}
