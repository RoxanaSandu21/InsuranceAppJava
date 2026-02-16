package org.example.insuranceapp.domain.metadata.risk;

import java.math.BigDecimal;

public class RiskFactorConfiguration {

    private Long id;
    private RiskLevel level;
    private Long referenceId;
    private BigDecimal adjustmentPercentage;
    private boolean active;

    public RiskFactorConfiguration(){}

    public RiskFactorConfiguration(RiskLevel level, Long referenceId, BigDecimal adjustmentPercentage, boolean active) {
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
