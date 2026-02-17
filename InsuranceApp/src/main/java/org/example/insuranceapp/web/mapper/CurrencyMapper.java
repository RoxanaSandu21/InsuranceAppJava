package org.example.insuranceapp.web.mapper;

import org.example.insuranceapp.domain.metadata.currency.Currency;
import org.example.insuranceapp.web.dto.metadata.CurrencyRequest;
import org.example.insuranceapp.web.dto.metadata.CurrencyResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {
    @Mapping(source = "active", target = "active")
    CurrencyResponse toResponse(Currency currency);

    @Mapping(source = "active", target = "active")
    @Mapping(target = "id", ignore = true)
    Currency toEntity(CurrencyRequest currencyRequest);
}