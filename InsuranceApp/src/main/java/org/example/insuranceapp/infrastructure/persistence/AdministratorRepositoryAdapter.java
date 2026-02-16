package org.example.insuranceapp.infrastructure.persistence;

import org.example.insuranceapp.domain.administrator.Administrator;
import org.example.insuranceapp.domain.administrator.AdministratorRepository;
import org.example.insuranceapp.infrastructure.persistence.entity.AdministratorEntity;
import org.example.insuranceapp.infrastructure.persistence.mapper.AdministratorPersistenceMapper;
import org.example.insuranceapp.infrastructure.persistence.repository.JpaAdministratorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AdministratorRepositoryAdapter implements AdministratorRepository {

    private final JpaAdministratorRepository jpaRepository;
    private final AdministratorPersistenceMapper mapper;

    public AdministratorRepositoryAdapter(JpaAdministratorRepository jpaRepository, AdministratorPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Administrator save(Administrator Administrator) {
        AdministratorEntity entity = mapper.toEntity(Administrator);
        AdministratorEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Administrator> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Page<Administrator> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(mapper::toDomain);
    }

}