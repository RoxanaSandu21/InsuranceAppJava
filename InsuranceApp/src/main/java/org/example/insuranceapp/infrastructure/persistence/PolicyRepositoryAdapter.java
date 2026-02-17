package org.example.insuranceapp.infrastructure.persistence;

import jakarta.persistence.criteria.Predicate;
import org.example.insuranceapp.domain.building.BuildingType;
import org.example.insuranceapp.domain.policy.Policy;
import org.example.insuranceapp.domain.policy.PolicyRepository;
import org.example.insuranceapp.domain.policy.PolicyStatus;
import org.example.insuranceapp.infrastructure.persistence.entity.PolicyEntity;
import org.example.insuranceapp.infrastructure.persistence.mapper.PolicyPersistenceMapper;
import org.example.insuranceapp.infrastructure.persistence.repository.JpaPolicyRepository;
import org.example.insuranceapp.infrastructure.report.ReportGrouping;
import org.example.insuranceapp.infrastructure.report.ReportGroupingStrategy;
import org.example.insuranceapp.web.dto.report.ReportDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class PolicyRepositoryAdapter implements PolicyRepository {

    private final JpaPolicyRepository jpaRepository;
    private final PolicyPersistenceMapper mapper;

    private final Map<ReportGrouping, ReportGroupingStrategy> strategies;

    public PolicyRepositoryAdapter(JpaPolicyRepository jpaRepository, PolicyPersistenceMapper mapper, List<ReportGroupingStrategy> strategyList) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
        this.strategies = strategyList.stream().collect(Collectors.toMap(ReportGroupingStrategy::getGroupingType, Function.identity()));
    }

    @Override
    public Policy save(Policy Policy) {
        PolicyEntity entity = mapper.toEntity(Policy);
        PolicyEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Policy> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Page<Policy> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public void deleteAll(){
        jpaRepository.deleteAll();
    }

    @Override
    public boolean existsById(Long id){
        return jpaRepository.existsById(id);
    }

    @Override
    public boolean existsByCurrencyIdAndPolicyStatus(Long id, PolicyStatus policyStatus) {
        return jpaRepository.existsByCurrencyIdAndPolicyStatus(id, policyStatus);
    }

    @Override
    public int expirePolicies(LocalDate today) {
        return jpaRepository.expirePolicies(today);
    }

    @Override
    public Page<Policy> search(Long clientId, Long brokerId, PolicyStatus policyStatus,
                               LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Specification<PolicyEntity> spec = (root, query, cb) -> {
            Predicate p = cb.conjunction();

            if (clientId != null) {
                p = cb.and(p, cb.equal(root.get("client").get("id"), clientId));
            }
            if (brokerId != null) {
                p = cb.and(p, cb.equal(root.get("broker").get("id"), brokerId));
            }
            if (policyStatus != null) {
                p = cb.and(p, cb.equal(root.get("policyStatus"), policyStatus));
            }
            if (startDate != null) {
                p = cb.and(p, cb.greaterThanOrEqualTo(root.get("startDate"), startDate));
            }
            if (endDate != null) {
                p = cb.and(p, cb.lessThanOrEqualTo(root.get("endDate"), endDate));
            }
            return p;
        };

        return jpaRepository.findAll(spec, pageable).map(mapper::toDomain);
    }

    @Override
    public List<ReportDto> getPolicyReport(ReportGrouping grouping, LocalDate from, LocalDate to, PolicyStatus status, Long currencyId, BuildingType buildingType) {
        ReportGroupingStrategy strategy = strategies.get(grouping);

        if (strategy == null) {
            throw new IllegalArgumentException("No strategy found for grouping: " + grouping);
        }

        return jpaRepository.getPolicyReport(strategy, from, to, status, currencyId, buildingType);
    }
}
