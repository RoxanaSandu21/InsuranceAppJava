package org.example.insuranceapp.application.service;

import org.example.insuranceapp.application.exception.NotFoundException;
import org.example.insuranceapp.application.exception.NotUniqueException;
import org.example.insuranceapp.domain.client.ClientRepository;
import org.example.insuranceapp.web.mapper.ClientMapper;
import org.example.insuranceapp.domain.client.Client;
import org.example.insuranceapp.domain.client.ClientType;
import org.example.insuranceapp.web.dto.client.ClientRequest;
import org.example.insuranceapp.web.dto.client.ClientResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceUnitTest {

    @Mock private ClientRepository clientRepository;

    @Mock private ClientMapper clientMapper;

    @InjectMocks private ClientService clientService;


    @Test
    void createClient_Success() {
        ClientRequest req = new ClientRequest(ClientType.INDIVIDUAL, "Ion", "123", "i@test.com", "0722", "Address");
        ClientResponse mockResponse = new ClientResponse(1L, ClientType.INDIVIDUAL, "Ion", "123", "i@test.com", "0722", "Address", null, null);
        when(clientRepository.existsByIdentificationNumber("123")).thenReturn(false);
        when(clientRepository.save(any(Client.class))).thenAnswer(inv -> {
            Client c = inv.getArgument(0);
            c.setId(1L);
            return c;
        });
        when(clientMapper.toResponse(any(Client.class))).thenReturn(mockResponse);

        ClientResponse res = clientService.createClient(req);

        assertNotNull(res);
        assertEquals(1L, res.id());
        verify(clientRepository).save(any(Client.class));
    }

    @Test
    void createClient_ThrowsNotUnique() {
        ClientRequest req = new ClientRequest(ClientType.INDIVIDUAL, "Ion", "123", "i@test.com", "0722", "Address");
        when(clientRepository.existsByIdentificationNumber("123")).thenReturn(true);

        assertThrows(NotUniqueException.class, () -> clientService.createClient(req));
        verify(clientRepository, never()).save(any());
    }

    @Test
    void searchClient_ById_Success() {
        Client client = new Client(ClientType.INDIVIDUAL, "Ion", "123", "i@test.com", "0722", "Address");
        client.setId(1L);
        ClientResponse mockResponse = new ClientResponse(1L, ClientType.INDIVIDUAL, "Ion", "123", "i@test.com", "0722", "Address", null, null);

        when(clientRepository.findByIdentificationNumber("123")).thenReturn(Optional.of(client));
        when(clientMapper.toResponse(client)).thenReturn(mockResponse);
        Page<ClientResponse> result = clientService.searchClient(null, "123", Pageable.unpaged());

        assertEquals(1, result.getTotalElements());
        assertEquals("123", result.getContent().get(0).identificationNumber());
    }

    @Test
    void searchClient_ByName_Success() {
        Client client = new Client(ClientType.INDIVIDUAL, "Ion Popescu", "123", "i@test.com", "0722", "Address");
        ClientResponse mockResponse = new ClientResponse(1L, ClientType.INDIVIDUAL, "Ion Popescu", "123", "i@test.com", "0722", "Address", null, null);

        Page<Client> page = new PageImpl<>(List.of(client));

        when(clientRepository.findByNameContainingIgnoreCase("Ion", Pageable.unpaged())).thenReturn(page);
        when(clientMapper.toResponse(client)).thenReturn(mockResponse);

        Page<ClientResponse> result = clientService.searchClient("Ion", null, Pageable.unpaged());

        assertEquals(1, result.getTotalElements());
        assertEquals("Ion Popescu", result.getContent().get(0).name());
    }

    @Test
    void searchClient_ById_NotFound() {
        Pageable pageable = Pageable.unpaged();
        when(clientRepository.findByIdentificationNumber("999")).thenReturn(Optional.empty());

        Page<ClientResponse> result = clientService.searchClient(null, "999", pageable);

        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void searchClient_ByName_NotFound() {
        Pageable pageable = Pageable.unpaged();
        when(clientRepository.findByNameContainingIgnoreCase("Unknown", pageable))
                .thenReturn(Page.empty());

        Page<ClientResponse> result = clientService.searchClient("Unknown", null, pageable);

        assertTrue(result.isEmpty());
    }

    @Test
    void searchClient_NoParams_ReturnsEmpty() {
        Pageable pageable = Pageable.unpaged();
        when(clientRepository.findAll(pageable)).thenReturn(Page.empty());

        Page<ClientResponse> result = clientService.searchClient(null, null, pageable);

        assertTrue(result.isEmpty());
    }

    @Test
    void updateClient_Success_NoIdChange() {
        Client existing = new Client(ClientType.INDIVIDUAL, "Old Name", "123", "old@test.com", "000", "Old Addr");
        existing.setId(1L);

        ClientRequest req = new ClientRequest(ClientType.INDIVIDUAL, "New Name", "123", "new@test.com", "111", "New Addr");
        ClientResponse mockResponse = new ClientResponse(1L, ClientType.INDIVIDUAL, "New Name", "123", "new@test.com", "111", "New Addr", null, null);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(clientRepository.save(any(Client.class))).thenAnswer(inv -> inv.getArgument(0));
        when(clientMapper.toResponse(any(Client.class))).thenReturn(mockResponse);

        ClientResponse res = clientService.updateClient(1L, req);

        assertEquals("New Name", res.name());
        assertEquals("123", res.identificationNumber());
        verify(clientRepository).save(existing);
    }

    @Test
    void updateClient_Success_WithIdChange() {
        Client existing = new Client(ClientType.INDIVIDUAL, "Ion", "123", "test", "00", "Ad");
        existing.setId(1L);
        existing.setNumberOfChangesOfIn(0);

        ClientRequest req = new ClientRequest(ClientType.INDIVIDUAL, "Ion", "456", "test", "00", "Ad");
        ClientResponse mockResponse = new ClientResponse(1L, ClientType.INDIVIDUAL, "Ion", "456", "test", "00", "Ad", null, null);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(clientRepository.existsByIdentificationNumber("456")).thenReturn(false);
        when(clientRepository.save(any(Client.class))).thenAnswer(inv -> inv.getArgument(0));
        when(clientMapper.toResponse(any(Client.class))).thenReturn(mockResponse);

        ClientResponse res = clientService.updateClient(1L, req);

        assertEquals("456", res.identificationNumber());
        assertEquals(1, existing.getNumberOfChangesOfIn());
    }

    @Test
    void updateClient_Throws_MaxChangesReached() {
        Client existing = new Client();
        existing.setId(1L);
        existing.setIdentificationNumber("100");
        existing.setNumberOfChangesOfIn(3);

        ClientRequest req = new ClientRequest(ClientType.INDIVIDUAL, "Ion", "200", "t", "0", "A");

        when(clientRepository.findById(1L)).thenReturn(Optional.of(existing));

        assertThrows(IllegalArgumentException.class, () -> clientService.updateClient(1L, req));
    }

    @Test
    void updateClient_Throws_NewIdNotUnique() {
        Client existing = new Client();
        existing.setId(1L);
        existing.setIdentificationNumber("100");
        existing.setNumberOfChangesOfIn(0);

        ClientRequest req = new ClientRequest(ClientType.INDIVIDUAL, "Ion", "200", "t", "0", "A");

        when(clientRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(clientRepository.existsByIdentificationNumber("200")).thenReturn(true);

        assertThrows(NotUniqueException.class, () -> clientService.updateClient(1L, req));
    }

    @Test
    void updateClient_NotFound() {
        ClientRequest req = new ClientRequest(ClientType.INDIVIDUAL, "New", "123", "e", "p", "a");
        when(clientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> clientService.updateClient(99L, req));
    }

    @Test
    void getDetails_Success() {
        Client client = new Client(ClientType.INDIVIDUAL, "Ion", "123", "i@test.com", "0722", "Address");
        client.setId(1L);
        ClientResponse mockResponse = new ClientResponse(1L, ClientType.INDIVIDUAL, "Ion", "123", "i@test.com", "0722", "Address", null, null);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientMapper.toResponse(any(Client.class))).thenReturn(mockResponse);

        ClientResponse res = clientService.getDetails(1L);

        assertNotNull(res);
        assertEquals(1L, res.id());
        assertEquals("Ion", res.name());
    }

    @Test
    void getDetails_NotFound() {
        when(clientRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> clientService.getDetails(99L));
    }

}