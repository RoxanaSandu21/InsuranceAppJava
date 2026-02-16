package org.example.insuranceapp.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.example.insuranceapp.domain.metadata.risk.RiskLevel;

import java.math.BigDecimal;

@Entity
@Table(name = "risk_factor_configurations")
public class RiskFactorConfigurationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RiskLevel level;

    private Long referenceId;
    private BigDecimal adjustmentPercentage;

    private boolean active;

    public RiskFactorConfigurationEntity(){}

    public RiskFactorConfigurationEntity(RiskLevel level, Long referenceId, BigDecimal adjustmentPercentage, boolean active) {
        this.level = level;
        this.referenceId = referenceId;
        this.adjustmentPercentage = adjustmentPercentage;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RiskLevel getLevel() {
        return level;
    }

    public void setLevel(RiskLevel level) {
        this.level = level;
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }

    public BigDecimal getAdjustmentPercentage() {
        return adjustmentPercentage;
    }

    public void setAdjustmentPercentage(BigDecimal adjustmentPercentage) {
        this.adjustmentPercentage = adjustmentPercentage;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
