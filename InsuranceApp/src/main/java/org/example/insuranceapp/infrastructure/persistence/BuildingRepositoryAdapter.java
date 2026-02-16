package org.example.insuranceapp.infrastructure.persistence;

import org.example.insuranceapp.domain.building.Building;
import org.example.insuranceapp.domain.building.BuildingRepository;
import org.example.insuranceapp.infrastructure.persistence.entity.BuildingEntity;
import org.example.insuranceapp.infrastructure.persistence.mapper.BuildingPersistenceMapper;
import org.example.insuranceapp.infrastructure.persistence.repository.JpaBuildingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class BuildingRepositoryAdapter implements BuildingRepository {

    private final JpaBuildingRepository jpaRepository;
    private final BuildingPersistenceMapper mapper;

    public BuildingRepositoryAdapter(JpaBuildingRepository jpaRepository, BuildingPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Building save(Building Building) {
        BuildingEntity entity = mapper.toEntity(Building);
        BuildingEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Building> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Page<Building> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public Page<Building> findByClientId(Long clientId, Pageable pageable){
        return jpaRepository.findByClientId(clientId, pageable).map(mapper::toDomain);
    }
}