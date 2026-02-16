package org.example.insuranceapp.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.example.insuranceapp.domain.metadata.fee.FeeType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "fee_configurations")
public class FeeConfigurationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FeeType type;

    @Column(nullable = false)
    private BigDecimal percentage;

    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private boolean active;

    public FeeConfigurationEntity(){}

    public FeeConfigurationEntity(String name, FeeType type, BigDecimal percentage, LocalDate effectiveFrom, LocalDate effectiveTo, boolean active) {
        this.name = name;
        this.type = type;
        this.percentage = percentage;
        this.effectiveFrom = effectiveFrom;
        this.effectiveTo = effectiveTo;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FeeType getType() {
        return type;
    }

    public void setType(FeeType type) {
        this.type = type;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public LocalDate getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(LocalDate effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public LocalDate getEffectiveTo() {
        return effectiveTo;
    }

    public void setEffectiveTo(LocalDate effectiveTo) {
        this.effectiveTo = effectiveTo;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}