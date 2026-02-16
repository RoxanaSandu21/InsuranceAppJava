package org.example.insuranceapp.infrastructure.report;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.From;
import org.example.insuranceapp.infrastructure.persistence.entity.PolicyEntity;

public interface ReportGroupingStrategy {
    Expression<String> getGroupingExpression(From<?, PolicyEntity> root);

    ReportGrouping getGroupingType();
}
