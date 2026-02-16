package org.example.insuranceapp.infrastructure.persistence;

import org.example.insuranceapp.domain.metadata.currency.Currency;
import org.example.insuranceapp.domain.metadata.currency.CurrencyRepository;
import org.example.insuranceapp.infrastructure.persistence.entity.CurrencyEntity;
import org.example.insuranceapp.infrastructure.persistence.mapper.CurrencyPersistenceMapper;
import org.example.insuranceapp.infrastructure.persistence.repository.JpaCurrencyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CurrencyRepositoryAdapter implements CurrencyRepository {

    private final JpaCurrencyRepository jpaRepository;
    private final CurrencyPersistenceMapper mapper;

    public CurrencyRepositoryAdapter(JpaCurrencyRepository jpaRepository, CurrencyPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Currency save(Currency Currency) {
        CurrencyEntity entity = mapper.toEntity(Currency);
        CurrencyEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Currency> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Page<Currency> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public boolean existsByCode(String code) {
        return jpaRepository.existsByCode(code);
    }
}