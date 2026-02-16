package org.example.insuranceapp.application.service;

import jakarta.transaction.Transactional;
import org.example.insuranceapp.application.exception.NotFoundException;
import org.example.insuranceapp.application.exception.NotUniqueException;
import org.example.insuranceapp.domain.metadata.risk.RiskFactorConfigurationRepository;
import org.example.insuranceapp.web.mapper.RiskFactorConfigurationMapper;
import org.example.insuranceapp.domain.metadata.risk.RiskFactorConfiguration;
import org.example.insuranceapp.web.dto.metadata.RiskFactorConfigurationRequest;
import org.example.insuranceapp.web.dto.metadata.RiskFactorConfigurationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RiskFactorConfigurationService {
    private final RiskFactorConfigurationRepository riskFactorConfigurationRepository;
    private final RiskFactorConfigurationMapper riskFactorConfigurationMapper;

    private static final Logger log = LoggerFactory.getLogger(RiskFactorConfigurationService.class);

    public RiskFactorConfigurationService(RiskFactorConfigurationRepository riskFactorConfigurationRepository, RiskFactorConfigurationMapper riskFactorConfigurationMapper){
        this.riskFactorConfigurationRepository = riskFactorConfigurationRepository;
        this.riskFactorConfigurationMapper = riskFactorConfigurationMapper;
    }

    public Page<RiskFactorConfigurationResponse> getRiskFactors(Pageable pageable){
        Page<RiskFactorConfiguration> riskFactorConfigurations = riskFactorConfigurationRepository.findAll(pageable);
        return riskFactorConfigurations.map(riskFactorConfigurationMapper::toResponse);
    }

    public RiskFactorConfigurationResponse createRiskFactor(RiskFactorConfigurationRequest riskFactorConfigurationRequest){
        if (riskFactorConfigurationRepository.existsByLevelAndReferenceIdAndActiveTrue(riskFactorConfigurationRequest.level(), riskFactorConfigurationRequest.referenceId())) {
            throw new NotUniqueException("An active risk configuration already exists for this level and reference.");
        }
        RiskFactorConfiguration riskFactorConfiguration = riskFactorConfigurationMapper.toEntity(riskFactorConfigurationRequest);
        RiskFactorConfiguration saved = riskFactorConfigurationRepository.save(riskFactorConfiguration);
        log.info("Risk configuration created with ID: {}", saved.getId());
        return riskFactorConfigurationMapper.toResponse(saved);
    }

    @Transactional
    public RiskFactorConfigurationResponse updateRiskFactor(Long riskId, RiskFactorConfigurationRequest riskFactorConfigurationRequest){
        RiskFactorConfiguration riskFactorConfiguration = riskFactorConfigurationRepository.findById(riskId).orElseThrow(() -> new NotFoundException("Risk configuration with id " + riskId));

        riskFactorConfiguration.setLevel(riskFactorConfigurationRequest.level());
        riskFactorConfiguration.setAdjustmentPercentage(riskFactorConfigurationRequest.adjustmentPercentage());
        riskFactorConfiguration.setReferenceId(riskFactorConfigurationRequest.referenceId());
        riskFactorConfiguration.setActive(riskFactorConfiguration.isActive());

        if (riskFactorConfigurationRequest.active() && riskFactorConfigurationRepository.countByLevelAndReferenceIdAndActiveTrue(riskFactorConfigurationRequest.level(), riskFactorConfigurationRequest.referenceId()) > 1) {
            throw new NotUniqueException("An active risk configuration already exists for this level and reference.");
        }
        RiskFactorConfiguration saved = riskFactorConfigurationRepository.save(riskFactorConfiguration);
        log.info("Risk configuration updated with ID: {}", saved.getId());
        return riskFactorConfigurationMapper.toResponse(saved);
    }
}
