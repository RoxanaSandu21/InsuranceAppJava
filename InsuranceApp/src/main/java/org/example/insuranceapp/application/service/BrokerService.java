package org.example.insuranceapp.application.service;

import jakarta.transaction.Transactional;
import org.example.insuranceapp.application.exception.NotFoundException;
import org.example.insuranceapp.application.exception.NotUniqueException;
import org.example.insuranceapp.domain.broker.BrokerRepository;
import org.example.insuranceapp.web.mapper.BrokerMapper;
import org.example.insuranceapp.domain.broker.Broker;
import org.example.insuranceapp.web.dto.broker.BrokerRequest;
import org.example.insuranceapp.web.dto.broker.BrokerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BrokerService {
    private final BrokerRepository brokerRepository;
    private final BrokerMapper brokerMapper;

    private static final Logger log = LoggerFactory.getLogger(BrokerService.class);

    private static final String BROKER_NOT_FOUND = "Broker with id ";

    public BrokerService(BrokerRepository brokerRepository, BrokerMapper brokerMapper){
        this.brokerRepository = brokerRepository;
        this.brokerMapper = brokerMapper;
    }

    public Page<BrokerResponse> getAllBrokers(Pageable pageable){
        Page<Broker> brokers = brokerRepository.findAll(pageable);
        return brokers.map(brokerMapper::toResponse);
    }

    public BrokerResponse viewBrokerDetails(Long brokerId){
        Broker broker = brokerRepository.findById(brokerId).orElseThrow(() -> new NotFoundException(BROKER_NOT_FOUND + brokerId));
        return brokerMapper.toResponse(broker);
    }

    @Transactional
    public BrokerResponse createBroker(BrokerRequest brokerRequest){
        if(brokerRepository.existsByBrokerCode(brokerRequest.brokerCode())){
            throw new NotUniqueException("broker");
        }

        Broker broker = new Broker();
        broker.setBrokerCode(brokerRequest.brokerCode());
        broker.setName(brokerRequest.name());
        broker.setEmail(brokerRequest.email());
        broker.setCommissionPercentage(brokerRequest.commissionPercentage());
        broker.setPhoneNumber(brokerRequest.phoneNumber());
        if(brokerRequest.active() != null){
            broker.setActive(brokerRequest.active());
        }

        Broker saved = brokerRepository.save(broker);
        log.info("Broker created with ID: {}", saved.getId());
        return brokerMapper.toResponse(saved);
    }

    @Transactional
    public BrokerResponse update(Long brokerId, BrokerRequest brokerRequest){
        Broker broker = brokerRepository.findById(brokerId).orElseThrow(() -> new NotFoundException(BROKER_NOT_FOUND + brokerId));
        broker.setEmail(brokerRequest.email());
        broker.setName(brokerRequest.name());
        broker.setPhoneNumber(brokerRequest.phoneNumber());
        broker.setCommissionPercentage(brokerRequest.commissionPercentage());

        Broker saved = brokerRepository.save(broker);
        log.info("Broker updated with ID: {}", brokerId);
        return brokerMapper.toResponse(saved);
    }

    public BrokerResponse activate(Long brokerId){
        Broker broker = brokerRepository.findById(brokerId).orElseThrow(() -> new NotFoundException(BROKER_NOT_FOUND + brokerId));
        broker.setActive(true);
        Broker saved = brokerRepository.save(broker);
        log.info("Broker activated with ID: {}", brokerId);
        return brokerMapper.toResponse(saved);
    }

    public BrokerResponse deactivate(Long brokerId){
        Broker broker = brokerRepository.findById(brokerId).orElseThrow(() -> new NotFoundException(BROKER_NOT_FOUND + brokerId));
        broker.setActive(false);
        Broker saved = brokerRepository.save(broker);
        log.info("Broker deactivated with ID: {}", brokerId);
        return brokerMapper.toResponse(saved);
    }


}
