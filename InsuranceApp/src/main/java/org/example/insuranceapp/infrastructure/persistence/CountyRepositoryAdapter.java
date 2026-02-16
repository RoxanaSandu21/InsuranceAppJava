package org.example.insuranceapp.infrastructure.persistence;

import org.example.insuranceapp.domain.geography.county.County;
import org.example.insuranceapp.domain.geography.county.CountyRepository;
import org.example.insuranceapp.infrastructure.persistence.entity.CountyEntity;
import org.example.insuranceapp.infrastructure.persistence.mapper.CountyPersistenceMapper;
import org.example.insuranceapp.infrastructure.persistence.repository.JpaCountyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public class CountyRepositoryAdapter implements CountyRepository {

    private final JpaCountyRepository jpaRepository;
    private final CountyPersistenceMapper mapper;

    public CountyRepositoryAdapter(JpaCountyRepository jpaRepository, CountyPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public County save(County County) {
        CountyEntity entity = mapper.toEntity(County);
        CountyEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<County> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Page<County> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public boolean existsById(Long countyId){
        return jpaRepository.existsById(countyId);
    }

    @Override
    public Page<County> findByCountryId(Long countryId, Pageable pageable) {
        return jpaRepository.findByCountryId(countryId, pageable).map(mapper::toDomain);
    }

}
