package org.example.insuranceapp.domain.geography.city;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CityRepository {
    City save(City city);
    Optional<City> findById(Long id);
    Page<City> findAll(Pageable pageable);
    Page<City> findByCountyId(Long countyId, Pageable pageable);
}
