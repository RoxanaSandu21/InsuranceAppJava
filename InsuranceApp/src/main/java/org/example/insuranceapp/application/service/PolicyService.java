package org.example.insuranceapp.application.service;

import jakarta.transaction.Transactional;
import org.example.insuranceapp.application.exception.NotFoundException;
import org.example.insuranceapp.domain.broker.BrokerRepository;
import org.example.insuranceapp.domain.building.BuildingRepository;
import org.example.insuranceapp.domain.metadata.currency.CurrencyRepository;
import org.example.insuranceapp.domain.policy.PolicyRepository;
import org.example.insuranceapp.infrastructure.persistence.ClientRepositoryAdapter;
import org.example.insuranceapp.web.mapper.PolicyDetailsMapper;
import org.example.insuranceapp.web.mapper.PolicyMapper;
import org.example.insuranceapp.domain.broker.Broker;
import org.example.insuranceapp.domain.building.Building;
import org.example.insuranceapp.domain.client.Client;
import org.example.insuranceapp.domain.metadata.currency.Currency;
import org.example.insuranceapp.domain.policy.Policy;
import org.example.insuranceapp.domain.policy.PolicyStatus;
import org.example.insuranceapp.web.dto.policy.PolicyDetails;
import org.example.insuranceapp.web.dto.policy.PolicyRequest;
import org.example.insuranceapp.web.dto.policy.PolicyResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class PolicyService {
    private final PolicyRepository policyRepository;
    private final PolicyMapper policyMapper;
    private final PolicyDetailsMapper policyDetailsMapper;
    private final ClientRepositoryAdapter clientRepositoryAdapter;
    private final BuildingRepository buildingRepository;
    private final BrokerRepository brokerRepository;
    private final CurrencyRepository currencyRepository;
    private final PremiumCalculationService premiumCalculationService;

    private static final Logger log = LoggerFactory.getLogger(PolicyService.class);

    private static final String POLICY_NOT_FOUND = "Policy with id ";

    public PolicyService(PolicyRepository policyRepository, PolicyMapper policyMapper, PolicyDetailsMapper policyDetailsMapper, ClientRepositoryAdapter clientRepositoryAdapter, BuildingRepository buildingRepository, BrokerRepository brokerRepository, CurrencyRepository currencyRepository, PremiumCalculationService premiumCalculationService){
        this.policyRepository = policyRepository;
        this.policyMapper = policyMapper;
        this.policyDetailsMapper = policyDetailsMapper;
        this.clientRepositoryAdapter = clientRepositoryAdapter;
        this.buildingRepository =buildingRepository;
        this.brokerRepository = brokerRepository;
        this.currencyRepository = currencyRepository;
        this.premiumCalculationService = premiumCalculationService;
    }

    public Page<PolicyResponse> searchPolicies(Long clientId, Long brokerId, PolicyStatus policyStatus,
                                               LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return policyRepository.search(clientId, brokerId, policyStatus, startDate, endDate, pageable)
                .map(policyMapper::toResponse);
    }

    public PolicyDetails viewDetails(Long policyId){
        Policy policy = policyRepository.findById(policyId).orElseThrow(() -> new NotFoundException(POLICY_NOT_FOUND + policyId));
        return policyDetailsMapper.toResponse(policy);
    }

    @Transactional
    public PolicyResponse createDraft(PolicyRequest policyRequest){
        Client client = clientRepositoryAdapter.findById(policyRequest.clientId())
                .orElseThrow(() -> new NotFoundException("Client with id " + policyRequest.clientId()));
        Building building = buildingRepository.findById(policyRequest.buildingId())
                .orElseThrow(() -> new NotFoundException("Building with id " + policyRequest.buildingId()));
        Broker broker = brokerRepository.findById(policyRequest.brokerId())
                .orElseThrow(() -> new NotFoundException("Broker with id " + policyRequest.brokerId()));
        Currency currency = currencyRepository.findById(policyRequest.currencyId())
                .orElseThrow(() -> new NotFoundException("Currency with id " + policyRequest.currencyId()));

        if (!building.getClient().getId().equals(client.getId())) {
            throw new IllegalArgumentException("Building does not belong to the given client.");
        }
        if (!broker.isActive()) {
            throw new IllegalStateException("Inactive broker cannot create policies.");
        }
        if (!currency.isActive()) {
            throw new IllegalStateException("Inactive currency cannot be used for new policies.");
        }

        if (policyRequest.endDate().isBefore(policyRequest.startDate())) {
            throw new IllegalArgumentException("End date must be after start date.");
        }

        if (policyRequest.basePremium().signum() <= 0) {
            throw new IllegalArgumentException("Base premium must be positive.");
        }

        Policy policy = policyMapper.toEntity(policyRequest);
        policy.setClient(client);
        policy.setBuilding(building);
        policy.setBroker(broker);
        policy.setCurrency(currency);
        policy.setPolicyStatus(PolicyStatus.DRAFT);

        BigDecimal finalPremium = premiumCalculationService.calculateFinalPremium(policy);
        policy.setFinalPremium(finalPremium);

        Policy saved  = policyRepository.save(policy);
        log.info("Policy draft with ID: {}", saved.getId());
        return policyMapper.toResponse(saved);
    }

    @Transactional
    public PolicyResponse activatePolicy(Long policyId) {
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new NotFoundException(POLICY_NOT_FOUND + policyId));

        if (policy.getPolicyStatus() != PolicyStatus.DRAFT) {
            throw new IllegalStateException("Only DRAFT policies can be activated.");
        }

        if (policy.getStartDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Start date is in the past.");
        }

        policy.setPolicyStatus(PolicyStatus.ACTIVE);
        Policy saved = policyRepository.save(policy);
        log.info("Policy {} activated successfully.", saved.getPolicyNumber());
        return policyMapper.toResponse(saved);
    }

    @Transactional
    public PolicyResponse cancelPolicy(Long policyId, String cancellationReason) {
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new NotFoundException(POLICY_NOT_FOUND + policyId));

        if (policy.getPolicyStatus() != PolicyStatus.ACTIVE) {
            throw new IllegalStateException("Only ACTIVE policies can be cancelled.");
        }

        policy.setPolicyStatus(PolicyStatus.CANCELLED);
        policy.setCancellationDate(LocalDateTime.now());
        policy.setCancellationReason(cancellationReason);

        Policy saved = policyRepository.save(policy);
        log.info("Policy {} cancelled for reason {}.", saved.getPolicyNumber(), cancellationReason);
        return policyMapper.toResponse(saved);
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void updateExpiredPolicies() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        int count = policyRepository.expirePolicies(yesterday);

        if (count > 0) {
            log.info("Cron Job: Successfully expired {} policies.", count);
        }
    }

}
