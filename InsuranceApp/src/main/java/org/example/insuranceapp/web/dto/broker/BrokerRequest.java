package org.example.insuranceapp.web.dto.broker;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record BrokerRequest(
        @NotBlank String brokerCode,
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank String phoneNumber,
        Boolean active,
        @PositiveOrZero BigDecimal commissionPercentage){
}
