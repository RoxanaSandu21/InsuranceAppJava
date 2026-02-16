package org.example.insuranceapp.infrastructure.report;

import org.example.insuranceapp.domain.building.BuildingType;
import org.example.insuranceapp.domain.policy.PolicyStatus;
import org.example.insuranceapp.web.dto.report.ReportDto;

import java.time.LocalDate;
import java.util.List;

public interface PolicyReportsRepository {
    List<ReportDto> getPolicyReport(
            ReportGroupingStrategy groupingStrategy,
            LocalDate from,
            LocalDate to,
            PolicyStatus status,
            Long currencyId,
            BuildingType buildingType
    );
    }
