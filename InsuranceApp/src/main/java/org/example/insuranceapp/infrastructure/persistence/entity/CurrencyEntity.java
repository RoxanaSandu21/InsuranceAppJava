package org.example.insuranceapp.infrastructure.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Entity
@Table(name = "currencies")
public class CurrencyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 3, unique = true, nullable = false)
    private String code;

    private String name;

    @Positive
    @Column(nullable = false)
    private BigDecimal exchangeRateToBase = BigDecimal.ONE;

    @Column(nullable = false)
    private boolean active = true;

    public CurrencyEntity(){}

    public CurrencyEntity(String code, String name, BigDecimal exchangeRateToBase, boolean active) {
        this.code = code;
        this.name = name;
        this.exchangeRateToBase = exchangeRateToBase;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getExchangeRateToBase() {
        return exchangeRateToBase;
    }

    public void setExchangeRateToBase(BigDecimal exchangeRateToBase) {
        this.exchangeRateToBase = exchangeRateToBase;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
