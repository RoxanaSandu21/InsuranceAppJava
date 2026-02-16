package org.example.insuranceapp.domain.building;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BuildingRepository {
    Building save(Building building);
    Optional<Building> findById(Long id);
    Page<Building> findAll(Pageable pageable);
    Page<Building> findByClientId(Long clientId, Pageable pageable);
}
