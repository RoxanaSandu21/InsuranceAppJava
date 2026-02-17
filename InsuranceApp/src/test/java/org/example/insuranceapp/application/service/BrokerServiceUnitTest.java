package org.example.insuranceapp.application.service;

import org.example.insuranceapp.application.exception.NotFoundException;
import org.example.insuranceapp.application.exception.NotUniqueException;
import org.example.insuranceapp.domain.broker.BrokerRepository;
import org.example.insuranceapp.web.mapper.BrokerMapper;
import org.example.insuranceapp.domain.broker.Broker;
import org.example.insuranceapp.web.dto.broker.BrokerRequest;
import org.example.insuranceapp.web.dto.broker.BrokerResponse;
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
class BrokerServiceUnitTest {

    @Mock
    private BrokerRepository brokerRepository;

    @Mock
    private BrokerMapper brokerMapper;

    @InjectMocks
    private BrokerService brokerService;

    private Broker mockBroker;
    private BrokerResponse mockResponse;

    @BeforeEach
    void setUp() {
        mockBroker = new Broker();
        mockBroker.setId(1L);
        mockBroker.setBrokerCode("BRK001");
        mockBroker.setName("Broker Test");
        mockBroker.setActive(true);

        mockResponse = new BrokerResponse(1L, "uuid-test", "BRK001", "Broker Test",
                "test@email.com", "0722222222", true, BigDecimal.TEN);
    }

    @Test
    void getAllBrokers_Success() {
        Page<Broker> brokerPage = new PageImpl<>(List.of(mockBroker));
        when(brokerRepository.findAll(any(Pageable.class))).thenReturn(brokerPage);
        when(brokerMapper.toResponse(mockBroker)).thenReturn(mockResponse);

        Page<BrokerResponse> result = brokerService.getAllBrokers(Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("BRK001", result.getContent().get(0).brokerCode());
    }

    @Test
    void createBroker_Success() {
        BrokerRequest request = new BrokerRequest("BRK001", "Broker Test", "test@email.com", "0722222222", true, BigDecimal.TEN);

        when(brokerRepository.existsByBrokerCode("BRK001")).thenReturn(false);
        when(brokerRepository.save(any(Broker.class))).thenReturn(mockBroker);
        when(brokerMapper.toResponse(any(Broker.class))).thenReturn(mockResponse);

        BrokerResponse result = brokerService.createBroker(request);

        assertNotNull(result);
        assertEquals("BRK001", result.brokerCode());
        verify(brokerRepository).save(any(Broker.class));
    }

    @Test
    void createBroker_ThrowsNotUniqueException() {
        BrokerRequest request = new BrokerRequest("BRK001", "Broker Test", "test@email.com", "0722222222", true, BigDecimal.TEN);
        when(brokerRepository.existsByBrokerCode("BRK001")).thenReturn(true);

        assertThrows(NotUniqueException.class, () -> brokerService.createBroker(request));
        verify(brokerRepository, never()).save(any());
    }

    @Test
    void updateBroker_Success() {
        BrokerRequest request = new BrokerRequest("BRK001", "Updated Name", "test@email.com", "0722222222", true, BigDecimal.TEN);
        when(brokerRepository.findById(1L)).thenReturn(Optional.of(mockBroker));
        when(brokerRepository.save(any(Broker.class))).thenReturn(mockBroker);
        when(brokerMapper.toResponse(mockBroker)).thenReturn(mockResponse);

        BrokerResponse result = brokerService.update(1L, request);

        assertNotNull(result);
        verify(brokerRepository).save(mockBroker);
    }

    @Test
    void activate_Success() {
        mockBroker.setActive(false);
        when(brokerRepository.findById(1L)).thenReturn(Optional.of(mockBroker));
        when(brokerRepository.save(any(Broker.class))).thenReturn(mockBroker);
        when(brokerMapper.toResponse(mockBroker)).thenReturn(mockResponse);

        BrokerResponse result = brokerService.activate(1L);

        assertTrue(mockBroker.isActive());
        assertNotNull(result);
    }

    @Test
    void deactivate_Success() {
        mockBroker.setActive(true);
        when(brokerRepository.findById(1L)).thenReturn(Optional.of(mockBroker));
        when(brokerRepository.save(any(Broker.class))).thenReturn(mockBroker);
        when(brokerMapper.toResponse(mockBroker)).thenReturn(mockResponse);

        BrokerResponse result = brokerService.deactivate(1L);

        assertFalse(mockBroker.isActive());
        assertNotNull(result);
    }

    @Test
    void findBroker_ThrowsNotFoundException() {
        when(brokerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> brokerService.viewBrokerDetails(99L));
    }
}