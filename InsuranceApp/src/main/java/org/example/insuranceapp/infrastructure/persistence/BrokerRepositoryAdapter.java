package org.example.insuranceapp.infrastructure.persistence;

import org.example.insuranceapp.domain.broker.Broker;
import org.example.insuranceapp.domain.broker.BrokerRepository;
import org.example.insuranceapp.infrastructure.persistence.entity.BrokerEntity;
import org.example.insuranceapp.infrastructure.persistence.mapper.BrokerPersistenceMapper;
import org.example.insuranceapp.infrastructure.persistence.repository.JpaBrokerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class BrokerRepositoryAdapter implements BrokerRepository {

    private final JpaBrokerRepository jpaRepository;
    private final BrokerPersistenceMapper mapper;

    public BrokerRepositoryAdapter(JpaBrokerRepository jpaRepository, BrokerPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Broker save(Broker Broker) {
        BrokerEntity entity = mapper.toEntity(Broker);
        BrokerEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Broker> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Page<Broker> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public boolean existsByBrokerCode(String brCode) {
        return jpaRepository.existsByBrokerCode(brCode);
    }

}