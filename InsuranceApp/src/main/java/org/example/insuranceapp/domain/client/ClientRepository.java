package org.example.insuranceapp.domain.client;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ClientRepository{
    Client save(Client client);
    Optional<Client> findById(Long id);
    Page<Client> findAll(Pageable pageable);
    boolean existsByIdentificationNumber(String identificationNumber);
    boolean existsById(Long clientId);
    Optional<Client> findByIdentificationNumber(String identificationNumber);
    Page<Client> findByNameContainingIgnoreCase(String name, Pageable pageable);
}