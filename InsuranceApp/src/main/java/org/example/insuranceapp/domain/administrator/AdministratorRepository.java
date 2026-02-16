package org.example.insuranceapp.domain.administrator;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AdministratorRepository {
    Administrator save(Administrator administrator);
    Optional<Administrator> findById(Long id);
    Page<Administrator> findAll(Pageable pageable);
}
