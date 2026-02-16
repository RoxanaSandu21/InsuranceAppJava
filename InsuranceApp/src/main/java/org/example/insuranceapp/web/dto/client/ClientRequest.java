package org.example.insuranceapp.web.dto.client;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.insuranceapp.domain.client.ClientType;

public record ClientRequest(
        @NotNull(message = "Type is required.") ClientType type,
        @NotBlank(message = "Name cannot be empty.") String name,
        @NotBlank(message = "Identification number cannot be empty.") String identificationNumber,
        @NotBlank(message = "Email cannot be empty.") @Email(message = "Email must be valid.") String email,
        @NotBlank(message = "Phone number cannot be empty.") String phoneNumber,
        String address
) {
}
