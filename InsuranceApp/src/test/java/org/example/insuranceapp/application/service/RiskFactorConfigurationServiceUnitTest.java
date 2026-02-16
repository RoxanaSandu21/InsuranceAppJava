package org.example.insuranceapp.application.service;

import org.example.insuranceapp.application.exception.NotFoundException;
import org.example.insuranceapp.application.exception.NotUniqueException;
import org.example.insuranceapp.web.mapper.RiskFactorConfigurationMapper;
import org.example.insuranceapp.domain.metadata.risk.RiskFactorConfiguration;
import org.example.insuranceapp.domain.metadata.risk.RiskLevel;
import org.example.insuranceapp.infrastructure.persistence.repository.JpaRiskFactorConfigurationRepository;
import org.example.insuranceapp.web.dto.metadata.RiskFactorConfigurationRequest;
import org.example.insuranceapp.web.dto.metadata.RiskFactorConfigurationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RiskFactorConfigurationServiceUnitTest {

    @Mock
    private JpaRiskFactorConfigurationRepository riskRepository;

    @Mock
    private RiskFactorConfigurationMapper riskMapper;

    @InjectMocks
    private RiskFactorConfigurationService riskService;

    private RiskFactorConfiguration mockRisk;
    private RiskFactorConfigurationResponse mockResponse;

    @BeforeEach
    void setUp() {
        mockRisk = new RiskFactorConfiguration(RiskLevel.CITY, 10L, BigDecimal.valueOf(5.0), true);
        mockRisk.setId(1L);

        mockResponse = new RiskFactorConfigurationResponse(1L, RiskLevel.CITY, 10L, BigDecimal.valueOf(5.0), true);
    }

    @Test
    void getRiskFactors_Success() {
        Page<RiskFactorConfiguration> page = new PageImpl<>(List.of(mockRisk));
        when(riskRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(riskMapper.toResponse(mockRisk)).thenReturn(mockResponse);

        Page<RiskFactorConfigurationResponse> result = riskService.getRiskFactors(Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(RiskLevel.CITY, result.getContent().get(0).level());
    }

    @Test
    void createRiskFactor_Success() {
        RiskFactorConfigurationRequest req = new RiskFactorConfigurationRequest(RiskLevel.COUNTY, 20L, BigDecimal.valueOf(3.5), true);

        when(riskRepository.existsByLevelAndReferenceIdAndActiveTrue(RiskLevel.COUNTY, 20L)).thenReturn(false);
        when(riskMapper.toEntity(req)).thenReturn(new RiskFactorConfiguration());
        when(riskRepository.save(any(RiskFactorConfiguration.class))).thenReturn(mockRisk);
        when(riskMapper.toResponse(any(RiskFactorConfiguration.class))).thenReturn(mockResponse);

        RiskFactorConfigurationResponse res = riskService.createRiskFactor(req);

        assertNotNull(res);
        verify(riskRepository).save(any(RiskFactorConfiguration.class));
    }

    @Test
    void createRiskFactor_ThrowsNotUniqueException() {
        RiskFactorConfigurationRequest req = new RiskFactorConfigurationRequest(RiskLevel.CITY, 10L, BigDecimal.TEN, true);

        when(riskRepository.existsByLevelAndReferenceIdAndActiveTrue(RiskLevel.CITY, 10L)).thenReturn(true);

        assertThrows(NotUniqueException.class, () -> riskService.createRiskFactor(req));
        verify(riskRepository, never()).save(any());
    }

    @Test
    void updateRiskFactor_Success() {
        RiskFactorConfigurationRequest req = new RiskFactorConfigurationRequest(RiskLevel.COUNTRY, 1L, BigDecimal.valueOf(2.0), true);

        when(riskRepository.findById(1L)).thenReturn(Optional.of(mockRisk));
        when(riskRepository.save(any(RiskFactorConfiguration.class))).thenReturn(mockRisk);
        when(riskMapper.toResponse(mockRisk)).thenReturn(mockResponse);

        RiskFactorConfigurationResponse res = riskService.updateRiskFactor(1L, req);

        assertNotNull(res);
        verify(riskRepository).save(mockRisk);
        assertEquals(RiskLevel.COUNTRY, mockRisk.getLevel());
    }

    @Test
    void updateRiskFactor_NotFound() {
        RiskFactorConfigurationRequest req = new RiskFactorConfigurationRequest(RiskLevel.CITY, 1L, BigDecimal.ONE, true);
        when(riskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> riskService.updateRiskFactor(99L, req));
    }
}