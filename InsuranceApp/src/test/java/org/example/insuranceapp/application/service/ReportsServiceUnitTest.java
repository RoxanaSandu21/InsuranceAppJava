package org.example.insuranceapp.application.service;

import org.example.insuranceapp.domain.building.BuildingType;
import org.example.insuranceapp.domain.policy.PolicyRepository;
import org.example.insuranceapp.domain.policy.PolicyStatus;
import org.example.insuranceapp.infrastructure.report.ReportGrouping;
import org.example.insuranceapp.web.dto.report.ReportDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportsServiceUnitTest {

    @Mock
    private PolicyRepository policyRepository;

    private ReportsService reportsService;

    @BeforeEach
    void setUp() {
        reportsService = new ReportsService(policyRepository);
    }

    @Test
    void getReport_Success() {
        LocalDate from = LocalDate.now().minusDays(10);
        LocalDate to = LocalDate.now();
        List<ReportDto> expectedReports = List.of(new ReportDto("Romania", "RON", 10L, BigDecimal.TEN, BigDecimal.TEN));

        when(policyRepository.getPolicyReport(eq(ReportGrouping.COUNTRY), any(), any(), any(), any(), any()))
                .thenReturn(expectedReports);

        List<ReportDto> result = reportsService.getReport(
                ReportGrouping.COUNTRY, from, to, PolicyStatus.ACTIVE, 1L, BuildingType.RESIDENTIAL
        );

        assertEquals(1, result.size());
        assertEquals("Romania", result.get(0).groupingKey());
        verify(policyRepository).getPolicyReport(ReportGrouping.COUNTRY, from, to, PolicyStatus.ACTIVE, 1L, BuildingType.RESIDENTIAL);
    }

    @Test
    void getReport_Fail_DateRangeInvalid() {
        LocalDate from = LocalDate.now();
        LocalDate to = LocalDate.now().minusDays(1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                reportsService.getReport(ReportGrouping.COUNTRY, from, to, null, null, null)
        );

        assertEquals("Start date cannot be after end date.", exception.getMessage());
        verifyNoInteractions(policyRepository);
    }

}