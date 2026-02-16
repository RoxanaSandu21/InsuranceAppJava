package org.example.insuranceapp.infrastructure.report;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.example.insuranceapp.domain.building.BuildingType;
import org.example.insuranceapp.domain.policy.PolicyStatus;
import org.example.insuranceapp.infrastructure.persistence.entity.CurrencyEntity;
import org.example.insuranceapp.infrastructure.persistence.entity.PolicyEntity;
import org.example.insuranceapp.web.dto.report.ReportDto;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PolicyReportsRepositoryImpl implements PolicyReportsRepository {

    @PersistenceContext
    private EntityManager em;

    public PolicyReportsRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<ReportDto> getPolicyReport(ReportGroupingStrategy groupingStrategy, LocalDate from, LocalDate to, PolicyStatus status, Long currencyId, BuildingType buildingType) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ReportDto> query = cb.createQuery(ReportDto.class);
        Root<PolicyEntity> policy = query.from(PolicyEntity.class);

        Expression<String> groupingExpression = groupingStrategy.getGroupingExpression(policy);

        Join<PolicyEntity, CurrencyEntity> currency = policy.join("currency");

        query.select(cb.construct(
                ReportDto.class,
                groupingExpression,
                currency.get("code"),
                cb.count(policy),
                cb.coalesce(cb.sum(policy.get("finalPremium")), BigDecimal.ZERO),
                cb.coalesce(cb.sum(cb.prod(policy.get("finalPremium"), currency.get("exchangeRateToBase"))), BigDecimal.ZERO)
        ));


        List<Predicate> predicates = new ArrayList<>();

        if (from != null) {
            predicates.add(cb.greaterThanOrEqualTo(policy.get("startDate"), from));
        }
        if (to != null) {
            predicates.add(cb.lessThanOrEqualTo(policy.get("endDate"), to));
        }
        if (status != null) {
            predicates.add(cb.equal(policy.get("policyStatus"), status));
        }
        if (currencyId != null) {
            predicates.add(cb.equal(currency.get("id"), currencyId));
        }
        if (buildingType != null) {
            predicates.add(cb.equal(policy.join("building").get("type"), buildingType));
        }

        query.where(predicates.toArray(new Predicate[0]));

        query.groupBy(groupingExpression, currency.get("code"));

        return em.createQuery(query).getResultList();
    }
}