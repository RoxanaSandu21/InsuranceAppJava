package org.example.insuranceapp.application.service;

import org.example.insuranceapp.application.exception.NotFoundException;
import org.example.insuranceapp.application.exception.NotUniqueException;
import org.example.insuranceapp.domain.metadata.currency.CurrencyRepository;
import org.example.insuranceapp.domain.policy.PolicyRepository;
import org.example.insuranceapp.web.mapper.CurrencyMapper;
import org.example.insuranceapp.domain.metadata.currency.Currency;
import org.example.insuranceapp.domain.policy.PolicyStatus;
import org.example.insuranceapp.web.dto.metadata.CurrencyRequest;
import org.example.insuranceapp.web.dto.metadata.CurrencyResponse;
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
class CurrencyServiceUnitTest {

    @Mock private CurrencyRepository currencyRepository;
    @Mock private CurrencyMapper currencyMapper;
    @Mock private PolicyRepository policyRepository;

    @InjectMocks private CurrencyService currencyService;

    private Currency mockCurrency;
    private CurrencyResponse mockResponse;

    @BeforeEach
    void setUp() {
        mockCurrency = new Currency("RON", "Romanian Leu", BigDecimal.ONE, true);
        mockCurrency.setId(1L);

        mockResponse = new CurrencyResponse(1L, "RON", "Romanian Leu", BigDecimal.ONE, true);
    }

    @Test
    void getAllCurrencies_Success() {
        Page<Currency> page = new PageImpl<>(List.of(mockCurrency));
        when(currencyRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(currencyMapper.toResponse(mockCurrency)).thenReturn(mockResponse);

        Page<CurrencyResponse> result = currencyService.getAllCurrencies(Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("RON", result.getContent().get(0).code());
    }

    @Test
    void createCurrency_Success() {
        CurrencyRequest req = new CurrencyRequest("EUR", "Euro", BigDecimal.valueOf(4.97), true);
        when(currencyRepository.existsByCode("EUR")).thenReturn(false);
        when(currencyMapper.toEntity(req)).thenReturn(new Currency());
        when(currencyRepository.save(any(Currency.class))).thenReturn(mockCurrency);
        when(currencyMapper.toResponse(any(Currency.class))).thenReturn(mockResponse);

        CurrencyResponse res = currencyService.createCurrency(req);

        assertNotNull(res);
        verify(currencyRepository).save(any(Currency.class));
    }

    @Test
    void createCurrency_ThrowsNotUniqueException() {
        CurrencyRequest req = new CurrencyRequest("RON", "Leu", BigDecimal.ONE, true);
        when(currencyRepository.existsByCode("RON")).thenReturn(true);

        assertThrows(NotUniqueException.class, () -> currencyService.createCurrency(req));
        verify(currencyRepository, never()).save(any());
    }

    @Test
    void updateCurrencyStatus_Deactivate_Success_NoActivePolicies() {
        when(currencyRepository.findById(1L)).thenReturn(Optional.of(mockCurrency));
        when(policyRepository.existsByCurrencyIdAndPolicyStatus(1L, PolicyStatus.ACTIVE)).thenReturn(false);
        when(currencyRepository.save(any(Currency.class))).thenReturn(mockCurrency);
        when(currencyMapper.toResponse(any(Currency.class))).thenReturn(mockResponse);

        currencyService.updateCurrencyStatus(1L, false);

        assertFalse(mockCurrency.isActive());
        verify(currencyRepository).save(mockCurrency);
    }

    @Test
    void updateCurrencyStatus_Deactivate_ThrowsException_WithActivePolicies() {
        mockCurrency.setActive(true);
        when(currencyRepository.findById(1L)).thenReturn(Optional.of(mockCurrency));
        when(policyRepository.existsByCurrencyIdAndPolicyStatus(1L, PolicyStatus.ACTIVE)).thenReturn(true);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> currencyService.updateCurrencyStatus(1L, false));

        assertEquals("Cannot deactivate currency: it is used in active policies", exception.getMessage());
        verify(currencyRepository, never()).save(any());
    }

    @Test
    void updateCurrencyStatus_Activate_Success() {
        mockCurrency.setActive(false);
        when(currencyRepository.findById(1L)).thenReturn(Optional.of(mockCurrency));
        when(currencyRepository.save(any(Currency.class))).thenReturn(mockCurrency);
        when(currencyMapper.toResponse(any(Currency.class))).thenReturn(mockResponse);

        currencyService.updateCurrencyStatus(1L, true);

        assertTrue(mockCurrency.isActive());
        verify(currencyRepository).save(mockCurrency);
        verify(policyRepository, never()).existsByCurrencyIdAndPolicyStatus(anyLong(), any());
    }

    @Test
    void updateCurrencyStatus_NotFound() {
        when(currencyRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> currencyService.updateCurrencyStatus(99L, true));
    }
}