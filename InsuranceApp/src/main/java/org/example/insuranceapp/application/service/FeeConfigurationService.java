package org.example.insuranceapp.application.service;

import jakarta.transaction.Transactional;
import org.example.insuranceapp.application.exception.NotFoundException;
import org.example.insuranceapp.domain.metadata.fee.FeeConfigurationRepository;
import org.example.insuranceapp.web.mapper.FeeConfigurationMapper;
import org.example.insuranceapp.domain.metadata.fee.FeeConfiguration;
import org.example.insuranceapp.web.dto.metadata.FeeConfigurationRequest;
import org.example.insuranceapp.web.dto.metadata.FeeConfigurationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class FeeConfigurationService {
    private final FeeConfigurationRepository feeConfigurationRepository;
    private final FeeConfigurationMapper feeConfigurationMapper;

    private static final Logger log = LoggerFactory.getLogger(FeeConfigurationService.class);

    public FeeConfigurationService(FeeConfigurationRepository feeConfigurationRepository, FeeConfigurationMapper feeConfigurationMapper){
        this.feeConfigurationRepository = feeConfigurationRepository;
        this.feeConfigurationMapper = feeConfigurationMapper;
    }

    public Page<FeeConfigurationResponse> getFees(Pageable pageable){
        Page<FeeConfiguration> feesPage = feeConfigurationRepository.findAll(pageable);
        return feesPage.map(feeConfigurationMapper::toResponse);
    }

    public FeeConfigurationResponse createFee(FeeConfigurationRequest feeConfigurationRequest){
        if(feeConfigurationRequest.effectiveFrom() != null && feeConfigurationRequest.effectiveTo() != null && feeConfigurationRequest.effectiveTo().isBefore(feeConfigurationRequest.effectiveFrom())) {
            throw new IllegalArgumentException("effectiveFrom must be <= effectiveTo");
        }
        FeeConfiguration feeConfiguration = feeConfigurationMapper.toEntity(feeConfigurationRequest);
        FeeConfiguration saved = feeConfigurationRepository.save(feeConfiguration);
        log.info("Fee configuration created with ID: {}", saved.getId());
        return feeConfigurationMapper.toResponse(saved);
    }

    @Transactional
    public FeeConfigurationResponse updateFee(Long feeId, FeeConfigurationRequest feeConfigurationRequest){
        FeeConfiguration feeConfiguration = feeConfigurationRepository.findById(feeId).orElseThrow(() -> new NotFoundException("Fee configuration with id " + feeId));

        if(feeConfigurationRequest.effectiveFrom() != null && feeConfigurationRequest.effectiveTo() != null && feeConfigurationRequest.effectiveFrom().isAfter(feeConfigurationRequest.effectiveTo())){
            throw new IllegalArgumentException("effectiveFrom must be <= effectiveTo");
        }
        feeConfiguration.setName(feeConfigurationRequest.name());
        feeConfiguration.setPercentage(feeConfigurationRequest.percentage());
        feeConfiguration.setType(feeConfigurationRequest.type());
        feeConfiguration.setActive(feeConfigurationRequest.active());
        feeConfiguration.setEffectiveFrom(feeConfigurationRequest.effectiveFrom());
        feeConfiguration.setEffectiveTo(feeConfigurationRequest.effectiveTo());

        FeeConfiguration saved = feeConfigurationRepository.save(feeConfiguration);
        log.info("Fee configuration updated with ID: {}", saved.getId());
        return feeConfigurationMapper.toResponse(saved);
    }

}
