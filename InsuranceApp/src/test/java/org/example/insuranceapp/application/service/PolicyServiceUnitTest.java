package org.example.insuranceapp.application.service;

import org.example.insuranceapp.application.exception.NotFoundException;
import org.example.insuranceapp.domain.broker.BrokerRepository;
import org.example.insuranceapp.domain.building.BuildingRepository;
import org.example.insuranceapp.domain.client.ClientRepository;
import org.example.insuranceapp.domain.metadata.currency.CurrencyRepository;
import org.example.insuranceapp.domain.policy.PolicyRepository;
import org.example.insuranceapp.web.mapper.PolicyDetailsMapper;
import org.example.insuranceapp.web.mapper.PolicyMapper;
import org.example.insuranceapp.domain.broker.Broker;
import org.example.insuranceapp.domain.building.Building;
import org.example.insuranceapp.domain.client.Client;
import org.example.insuranceapp.domain.metadata.currency.Currency;
import org.example.insuranceapp.domain.policy.Policy;
import org.example.insuranceapp.domain.policy.PolicyStatus;
import org.example.insuranceapp.web.dto.policy.PolicyRequest;
import org.example.insuranceapp.web.dto.policy.PolicyResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PolicyServiceUnitTest {
    @Mock private PolicyRepository policyRepository;
    @Mock private PolicyMapper policyMapper;
    @Mock private PolicyDetailsMapper policyDetailsMapper;
    @Mock private ClientRepository clientRepository;
    @Mock private BuildingRepository buildingRepository;
    @Mock private BrokerRepository brokerRepository;
    @Mock private CurrencyRepository currencyRepository;
    @Mock private PremiumCalculationService premiumCalculationService;

    @InjectMocks
    private PolicyService policyService;

    private Client mockClient;
    private Building mockBuilding;
    private Broker mockBroker;
    private Currency mockCurrency;
    private Policy mockPolicy;

    @BeforeEach
    void setUp() {
        mockClient = new Client();
        mockClient.setId(1L);

        mockBuilding = new Building();
        mockBuilding.setId(10L);
        mockBuilding.setClient(mockClient);

        mockBroker = new Broker();
        mockBroker.setId(100L);
        mockBroker.setActive(true);

        mockCurrency = new Currency();
        mockCurrency.setId(5L);
        mockCurrency.setActive(true);

        mockPolicy = new Policy();
        mockPolicy.setId(1000L);
        mockPolicy.setPolicyStatus(PolicyStatus.DRAFT);
        mockPolicy.setStartDate(LocalDate.now().plusDays(1));
    }

    @Test
    void createDraft_Success() {
        PolicyRequest req = new PolicyRequest(1L, 10L, 100L, LocalDate.now(), LocalDate.now().plusYears(1), BigDecimal.valueOf(100), 5L);
        Policy entity = new Policy();
        PolicyResponse mockRes = new PolicyResponse(1000L, "POL-123", 1L, 10L, 100L, PolicyStatus.DRAFT, null, null, null, null, 5L, null, null, null);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(mockClient));
        when(buildingRepository.findById(10L)).thenReturn(Optional.of(mockBuilding));
        when(brokerRepository.findById(100L)).thenReturn(Optional.of(mockBroker));
        when(currencyRepository.findById(5L)).thenReturn(Optional.of(mockCurrency));

        when(policyMapper.toEntity(any())).thenReturn(entity);
        when(premiumCalculationService.calculateFinalPremium(any())).thenReturn(BigDecimal.valueOf(120));
        when(policyRepository.save(any())).thenReturn(mockPolicy);
        when(policyMapper.toResponse(any())).thenReturn(mockRes);

        PolicyResponse result = policyService.createDraft(req);

        assertNotNull(result);
        assertEquals(PolicyStatus.DRAFT, result.policyStatus());
        verify(policyRepository).save(any(Policy.class));
    }

    @Test
    void createDraft_ThrowsException_WhenBuildingNotOwnedByClient() {
        Client differentClient = new Client();
        differentClient.setId(99L);
        mockBuilding.setClient(differentClient);

        PolicyRequest req = new PolicyRequest(1L, 10L, 100L, LocalDate.now(), LocalDate.now().plusYears(1), BigDecimal.valueOf(100), 5L);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(mockClient));
        when(buildingRepository.findById(10L)).thenReturn(Optional.of(mockBuilding));

        assertThrows(NotFoundException.class, () -> policyService.createDraft(req));
    }

    @Test
    void activatePolicy_Success() {
        when(policyRepository.findById(1000L)).thenReturn(Optional.of(mockPolicy));
        when(policyRepository.save(any())).thenReturn(mockPolicy);

        PolicyResponse mockRes = new PolicyResponse(1000L, "POL-123", 1L, 10L, 100L, PolicyStatus.ACTIVE, null, null, null, null, null, null, null, null);
        when(policyMapper.toResponse(any())).thenReturn(mockRes);

        PolicyResponse result = policyService.activatePolicy(1000L);

        assertEquals(PolicyStatus.ACTIVE, result.policyStatus());
        verify(policyRepository).save(mockPolicy);
    }

    @Test
    void activatePolicy_ThrowsException_IfDateInPast() {
        mockPolicy.setStartDate(LocalDate.now().minusDays(5));
        when(policyRepository.findById(1000L)).thenReturn(Optional.of(mockPolicy));

        assertThrows(IllegalArgumentException.class, () -> policyService.activatePolicy(1000L));
    }

    @Test
    void cancelPolicy_Success() {
        mockPolicy.setPolicyStatus(PolicyStatus.ACTIVE);
        when(policyRepository.findById(1000L)).thenReturn(Optional.of(mockPolicy));
        when(policyRepository.save(any())).thenReturn(mockPolicy);

        PolicyResponse mockRes = new PolicyResponse(1000L, "POL-123", 1L, 10L, 100L, PolicyStatus.CANCELLED, null, null, null, null, null, null, null, null);
        when(policyMapper.toResponse(any())).thenReturn(mockRes);

        PolicyResponse result = policyService.cancelPolicy(1000L, "Reason");

        assertEquals(PolicyStatus.CANCELLED, result.policyStatus());
        assertNotNull(mockPolicy.getCancellationDate());
    }

    @Test
    void cancelPolicy_ThrowsException_IfNotActive() {
        mockPolicy.setPolicyStatus(PolicyStatus.DRAFT);
        when(policyRepository.findById(1000L)).thenReturn(Optional.of(mockPolicy));

        assertThrows(IllegalStateException.class, () -> policyService.cancelPolicy(1000L, "Reason"));
    }

    @Test
    void updateExpiredPolicies_Success() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        when(policyRepository.expirePolicies(yesterday)).thenReturn(5);

        policyService.updateExpiredPolicies();

        verify(policyRepository).expirePolicies(yesterday);
    }
}