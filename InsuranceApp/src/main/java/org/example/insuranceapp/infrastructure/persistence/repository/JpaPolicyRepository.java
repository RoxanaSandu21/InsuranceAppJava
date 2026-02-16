package org.example.insuranceapp.infrastructure.persistence.repository;

import org.example.insuranceapp.domain.policy.PolicyStatus;
import org.example.insuranceapp.infrastructure.persistence.entity.PolicyEntity;
import org.example.insuranceapp.infrastructure.report.PolicyReportsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface JpaPolicyRepository extends JpaRepository<PolicyEntity, Long>, JpaSpecificationExecutor<PolicyEntity>, PolicyReportsRepository {
    boolean existsByCurrencyIdAndPolicyStatus(Long id, PolicyStatus policyStatus);

    @Modifying
    @Query("UPDATE PolicyEntity p SET p.policyStatus = 'EXPIRED', p.updatedAt = CURRENT_TIMESTAMP " +
            "WHERE p.policyStatus = 'ACTIVE' AND p.endDate < :today")
    int expirePolicies(@Param("today") LocalDate today);
}
