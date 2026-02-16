package org.example.insuranceapp.domain.broker;

import java.math.BigDecimal;

public class Broker {

    private Long id;
    private String uniqueIdentifier;
    private String brokerCode;
    private String name;
    private String email;
    private String phoneNumber;
    private boolean active = true;
    private BigDecimal commissionPercentage;

    public Broker(){}

    public Broker(String brokerCode, String name, String email, String phoneNumber, boolean active, BigDecimal commissionPercentage) {
        this.brokerCode = brokerCode;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.active = active;
        this.commissionPercentage = commissionPercentage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public void setUniqueIdentifier(String uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
    }

    public String getBrokerCode() {
        return brokerCode;
    }

    public void setBrokerCode(String brokerCode) {
        this.brokerCode = brokerCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public BigDecimal getCommissionPercentage() {
        return commissionPercentage;
    }

    public void setCommissionPercentage(BigDecimal commissionPercentage) {
        this.commissionPercentage = commissionPercentage;
    }
}
