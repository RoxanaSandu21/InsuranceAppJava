package org.example.insuranceapp.application.service;

import org.example.insuranceapp.application.exception.NotFoundException;
import org.example.insuranceapp.domain.metadata.fee.FeeConfigurationRepository;
import org.example.insuranceapp.web.mapper.FeeConfigurationMapper;
import org.example.insuranceapp.domain.metadata.fee.FeeConfiguration;
import org.example.insuranceapp.domain.metadata.fee.FeeType;
import org.example.insuranceapp.web.dto.metadata.FeeConfigurationRequest;
import org.example.insuranceapp.web.dto.metadata.FeeConfigurationResponse;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeeConfigurationServiceUnitTest {

    @Mock
    private FeeConfigurationRepository feeConfigurationRepository;

    @Mock
    private FeeConfigurationMapper feeConfigurationMapper;

    @InjectMocks
    private FeeConfigurationService feeConfigurationService;

    private FeeConfiguration mockFee;
    private FeeConfigurationResponse mockResponse;

    @BeforeEach
    void setUp() {
        mockFee = new FeeConfiguration("Standard Broker Fee", FeeType.BROKER_COMMISSION,
                BigDecimal.valueOf(10.0), LocalDate.now(), null, true);
        mockFee.setId(1L);

        mockResponse = new FeeConfigurationResponse(1L, "Standard Broker Fee", FeeType.BROKER_COMMISSION,
                BigDecimal.valueOf(10.0), LocalDate.now(), null, true);
    }

    @Test
    void getFees_Success() {
        Page<FeeConfiguration> page = new PageImpl<>(List.of(mockFee));
        when(feeConfigurationRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(feeConfigurationMapper.toResponse(mockFee)).thenReturn(mockResponse);

        Page<FeeConfigurationResponse> result = feeConfigurationService.getFees(Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Standard Broker Fee", result.getContent().get(0).name());
    }

    @Test
    void createFee_Success() {
        FeeConfigurationRequest req = new FeeConfigurationRequest("New Fee", FeeType.ADMIN_FEE,
                BigDecimal.valueOf(5.0), null, null, true);

        when(feeConfigurationMapper.toEntity(req)).thenReturn(new FeeConfiguration());
        when(feeConfigurationRepository.save(any(FeeConfiguration.class))).thenReturn(mockFee);
        when(feeConfigurationMapper.toResponse(any(FeeConfiguration.class))).thenReturn(mockResponse);

        FeeConfigurationResponse res = feeConfigurationService.createFee(req);

        assertNotNull(res);
        verify(feeConfigurationRepository).save(any(FeeConfiguration.class));
    }

    @Test
    void updateFee_Success() {
        FeeConfigurationRequest req = new FeeConfigurationRequest("Updated Fee", FeeType.RISK_ADJUSTMENT,
                BigDecimal.valueOf(12.0),
                LocalDate.now(),
                LocalDate.now().plusMonths(1), true);

        when(feeConfigurationRepository.findById(1L)).thenReturn(Optional.of(mockFee));
        when(feeConfigurationRepository.save(any(FeeConfiguration.class))).thenReturn(mockFee);
        when(feeConfigurationMapper.toResponse(mockFee)).thenReturn(mockResponse);

        FeeConfigurationResponse res = feeConfigurationService.updateFee(1L, req);

        assertNotNull(res);
        verify(feeConfigurationRepository).save(mockFee);
        assertEquals("Updated Fee", mockFee.getName());
    }

    @Test
    void updateFee_ThrowsException_InvalidDateRange() {
        FeeConfigurationRequest req = new FeeConfigurationRequest("Invalid Date Fee", FeeType.ADMIN_FEE,
                BigDecimal.ONE,
                LocalDate.now().plusDays(10),
                LocalDate.now(), true);

        when(feeConfigurationRepository.findById(1L)).thenReturn(Optional.of(mockFee));

        assertThrows(IllegalArgumentException.class, () -> feeConfigurationService.updateFee(1L, req));
        verify(feeConfigurationRepository, never()).save(any());
    }

    @Test
    void updateFee_NotFound() {
        FeeConfigurationRequest req = new FeeConfigurationRequest("Any", FeeType.ADMIN_FEE, BigDecimal.ONE, null, null, true);
        when(feeConfigurationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> feeConfigurationService.updateFee(99L, req));
    }
}