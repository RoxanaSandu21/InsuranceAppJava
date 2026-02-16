package org.example.insuranceapp.infrastructure.persistence;

import org.example.insuranceapp.domain.geography.country.Country;
import org.example.insuranceapp.domain.geography.country.CountryRepository;
import org.example.insuranceapp.infrastructure.persistence.entity.CountryEntity;
import org.example.insuranceapp.infrastructure.persistence.mapper.CountryPersistenceMapper;
import org.example.insuranceapp.infrastructure.persistence.repository.JpaCountryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CountryRepositoryAdapter implements CountryRepository {

    private final JpaCountryRepository jpaRepository;
    private final CountryPersistenceMapper mapper;

    public CountryRepositoryAdapter(JpaCountryRepository jpaRepository, CountryPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Country save(Country Country) {
        CountryEntity entity = mapper.toEntity(Country);
        CountryEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Country> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Page<Country> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public boolean existsById(Long countryId){
        return jpaRepository.existsById(countryId);
    }
}