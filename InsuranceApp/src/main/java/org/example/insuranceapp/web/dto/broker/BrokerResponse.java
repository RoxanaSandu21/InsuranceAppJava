package org.example.insuranceapp.web.dto.broker;

import java.math.BigDecimal;

public record BrokerResponse (Long id, String uniqueIdentifier, String brokerCode, String name, String email, String phoneNumber, boolean active, BigDecimal commissionPercentage){
}
