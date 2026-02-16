package org.example.insuranceapp.infrastructure.persistence.mapper;

import org.example.insuranceapp.domain.administrator.Administrator;
import org.example.insuranceapp.infrastructure.persistence.entity.AdministratorEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdministratorPersistenceMapper {
    AdministratorEntity toEntity(Administrator administrator);
    Administrator toDomain(AdministratorEntity entity);
}
