package org.example.insuranceapp.infrastructure.report;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.From;
import org.example.insuranceapp.infrastructure.persistence.entity.PolicyEntity;
import org.springframework.stereotype.Component;

@Component
public class BrokerGroupingStrategy implements ReportGroupingStrategy {
    @Override
    public Expression<String> getGroupingExpression(From<?, PolicyEntity> root) {
        return root.join("broker")
                .get("name");
    }

    @Override
    public ReportGrouping getGroupingType() {
        return ReportGrouping.BROKER;
    }
}