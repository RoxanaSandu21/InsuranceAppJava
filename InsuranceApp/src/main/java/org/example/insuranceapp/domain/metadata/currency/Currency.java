package org.example.insuranceapp.domain.metadata.currency;

import java.math.BigDecimal;

public class Currency {
    private Long id;
    private String code;
    private String name;
    private BigDecimal exchangeRateToBase = BigDecimal.ONE;
    private boolean active = true;

    public Currency(){}

    public Currency(String code, String name, BigDecimal exchangeRateToBase, boolean active) {
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
