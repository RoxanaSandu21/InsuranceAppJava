package org.example.insuranceapp.domain.policy;

import org.example.insuranceapp.domain.building.BuildingType;
import org.example.insuranceapp.infrastructure.report.ReportGrouping;
import org.example.insuranceapp.web.dto.report.ReportDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PolicyRepository {
    Policy save(Policy policy);
    Optional<Policy> findById(Long id);
    Page<Policy> findAll(Pageable pageable);

    boolean existsByCurrencyIdAndPolicyStatus(Long id, PolicyStatus policyStatus);
    int expirePolicies(LocalDate today);
    Page<Policy> search(Long clientId, Long brokerId, PolicyStatus policyStatus,
                        LocalDate startDate, LocalDate endDate, Pageable pageable);

    List<ReportDto> getPolicyReport(ReportGrouping grouping,
                                    LocalDate from,
                                    LocalDate to,
                                    PolicyStatus status,
                                    Long currencyId,
                                    BuildingType buildingType);
}
