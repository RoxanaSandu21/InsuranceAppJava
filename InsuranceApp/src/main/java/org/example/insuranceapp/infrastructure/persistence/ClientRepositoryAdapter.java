package org.example.insuranceapp.infrastructure.persistence;

import org.example.insuranceapp.domain.client.Client;
import org.example.insuranceapp.domain.client.ClientRepository;
import org.example.insuranceapp.infrastructure.persistence.entity.ClientEntity;
import org.example.insuranceapp.infrastructure.persistence.mapper.ClientPersistenceMapper;
import org.example.insuranceapp.infrastructure.persistence.repository.JpaClientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ClientRepositoryAdapter implements ClientRepository {

    private final JpaClientRepository jpaRepository;
    private final ClientPersistenceMapper mapper;

    public ClientRepositoryAdapter(JpaClientRepository jpaRepository, ClientPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Client save(Client client) {
        ClientEntity entity = mapper.toEntity(client);
        ClientEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Client> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Page<Client> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public List<Client> findAll(){return jpaRepository.findAll().stream().map(mapper::toDomain).toList();}

    @Override
    public void deleteAll(){
        jpaRepository.deleteAll();
    }

    @Override
    public long count(){
        return jpaRepository.count();
    }

    @Override
    public boolean existsByIdentificationNumber(String idNum) {
        return jpaRepository.existsByIdentificationNumber(idNum);
    }

    @Override
    public boolean existsById(Long clientId){
        return jpaRepository.existsById(clientId);
    }

    @Override
    public Optional<Client> findByIdentificationNumber(String identificationNumber){
        return jpaRepository.findByIdentificationNumber(identificationNumber).map(mapper::toDomain);
    }

    @Override
    public Page<Client> findByNameContainingIgnoreCase(String name, Pageable pageable){
        return jpaRepository.findByNameContainingIgnoreCase(name, pageable).map(mapper::toDomain);
    }

}