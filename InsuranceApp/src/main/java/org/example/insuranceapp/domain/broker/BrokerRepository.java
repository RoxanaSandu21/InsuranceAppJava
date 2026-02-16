package org.example.insuranceapp.domain.broker;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BrokerRepository {
    Broker save(Broker broker);
    Optional<Broker> findById(Long id);
    Page<Broker> findAll(Pageable pageable);
    boolean existsByBrokerCode(String s);
}
