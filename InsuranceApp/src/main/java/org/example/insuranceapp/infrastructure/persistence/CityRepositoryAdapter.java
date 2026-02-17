package org.example.insuranceapp.infrastructure.persistence;

import org.example.insuranceapp.domain.geography.city.City;
import org.example.insuranceapp.domain.geography.city.CityRepository;
import org.example.insuranceapp.infrastructure.persistence.entity.CityEntity;
import org.example.insuranceapp.infrastructure.persistence.mapper.CityPersistenceMapper;
import org.example.insuranceapp.infrastructure.persistence.repository.JpaCityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CityRepositoryAdapter implements CityRepository {

    private final JpaCityRepository jpaRepository;
    private final CityPersistenceMapper mapper;

    public CityRepositoryAdapter(JpaCityRepository jpaRepository, CityPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public City save(City City) {
        CityEntity entity = mapper.toEntity(City);
        CityEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<City> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Page<City> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public void deleteAll(){
        jpaRepository.deleteAll();
    }

    @Override
    public Page<City> findByCountyId(Long countyId, Pageable pageable){
        return jpaRepository.findByCountyId(countyId, pageable).map(mapper::toDomain);
    }

}