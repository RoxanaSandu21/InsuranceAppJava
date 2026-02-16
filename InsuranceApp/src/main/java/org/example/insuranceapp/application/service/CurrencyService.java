package org.example.insuranceapp.application.service;

import jakarta.transaction.Transactional;
import org.example.insuranceapp.application.exception.NotFoundException;
import org.example.insuranceapp.application.exception.NotUniqueException;
import org.example.insuranceapp.domain.metadata.currency.CurrencyRepository;
import org.example.insuranceapp.domain.policy.PolicyRepository;
import org.example.insuranceapp.web.mapper.CurrencyMapper;
import org.example.insuranceapp.domain.metadata.currency.Currency;
import org.example.insuranceapp.domain.policy.PolicyStatus;
import org.example.insuranceapp.web.dto.metadata.CurrencyRequest;
import org.example.insuranceapp.web.dto.metadata.CurrencyResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CurrencyService {
    private final CurrencyRepository currencyRepository;
    private final CurrencyMapper currencyMapper;
    private final PolicyRepository policyRepository;

    private static final Logger log = LoggerFactory.getLogger(CurrencyService.class);

    public CurrencyService(CurrencyRepository currencyRepository, CurrencyMapper currencyMapper, PolicyRepository policyRepository){
        this.currencyRepository = currencyRepository;
        this.currencyMapper = currencyMapper;
        this.policyRepository = policyRepository;
    }

    public Page<CurrencyResponse> getAllCurrencies(Pageable pageable){
        Page<Currency> currencyPage = currencyRepository.findAll(pageable);
        return currencyPage.map(currencyMapper::toResponse);
    }

    public CurrencyResponse createCurrency(CurrencyRequest currencyRequest){
        if(currencyRepository.existsByCode(currencyRequest.code())){
            throw new NotUniqueException("currency");
        }
        Currency currency = currencyMapper.toEntity(currencyRequest);
        Currency saved = currencyRepository.save(currency);
        log.info("Currency created with ID: {}", saved.getId());
        return currencyMapper.toResponse(saved);
    }

    @Transactional
    public CurrencyResponse updateCurrencyStatus(Long currencyId, boolean isActive){
        Currency currency = currencyRepository.findById(currencyId).orElseThrow(() -> new NotFoundException("Currency with id " + currencyId));

        if(currency.isActive() && !isActive){
            boolean hasActivePolicies = policyRepository.existsByCurrencyIdAndPolicyStatus(currency.getId(), PolicyStatus.ACTIVE);
            if(hasActivePolicies){
                throw new IllegalStateException("Cannot deactivate currency: it is used in active policies");
            }
        }

        currency.setActive(isActive);
        Currency saved = currencyRepository.save(currency);
        log.info("Currency status updated in {} with ID: {}",saved.isActive(), saved.getId());
        return currencyMapper.toResponse(saved);
    }
}
