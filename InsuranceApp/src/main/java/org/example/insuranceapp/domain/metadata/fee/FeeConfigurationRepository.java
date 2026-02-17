package org.example.insuranceapp.domain.metadata.fee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface FeeConfigurationRepository {
    FeeConfiguration save(FeeConfiguration feeConfiguration);
    Optional<FeeConfiguration> findById(Long id);
    Page<FeeConfiguration> findAll(Pageable pageable);
    void deleteAll();
    List<FeeConfiguration> findAllByActiveTrue();

    int count();
}
