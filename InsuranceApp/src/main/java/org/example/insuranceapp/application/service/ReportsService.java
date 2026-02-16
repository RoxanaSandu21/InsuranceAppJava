package org.example.insuranceapp.application.service;

import org.example.insuranceapp.domain.building.BuildingType;
import org.example.insuranceapp.domain.policy.PolicyRepository;
import org.example.insuranceapp.domain.policy.PolicyStatus;
import org.example.insuranceapp.infrastructure.report.ReportGrouping;
import org.example.insuranceapp.infrastructure.report.ReportGroupingStrategy;
import org.example.insuranceapp.web.dto.report.ReportDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReportsService {

    private final PolicyRepository policyRepository;

    public ReportsService(PolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    public List<ReportDto> getReport(ReportGrouping type, LocalDate from, LocalDate to, PolicyStatus status, Long currencyId, BuildingType buildingType) {
        if (from != null && to != null && from.isAfter(to)) {
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }
        return policyRepository.getPolicyReport(type, from, to, status, currencyId, buildingType);
    }
}