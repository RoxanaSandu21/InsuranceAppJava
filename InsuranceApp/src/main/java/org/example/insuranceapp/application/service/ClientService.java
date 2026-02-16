package org.example.insuranceapp.application.service;

import jakarta.transaction.Transactional;
import org.example.insuranceapp.domain.client.ClientRepository;
import org.example.insuranceapp.web.mapper.ClientMapper;
import org.example.insuranceapp.domain.client.Client;
import org.example.insuranceapp.web.dto.client.ClientRequest;
import org.example.insuranceapp.web.dto.client.ClientResponse;
import org.example.insuranceapp.application.exception.NotFoundException;
import org.example.insuranceapp.application.exception.NotUniqueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    private static final Logger log = LoggerFactory.getLogger(ClientService.class);

    public ClientService(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    public ClientResponse createClient(ClientRequest clientRequest) {
        if (clientRepository.existsByIdentificationNumber(clientRequest.identificationNumber()))
            throw new NotUniqueException("identification number");

        Client client = new Client(clientRequest.type(), clientRequest.name(), clientRequest.identificationNumber(), clientRequest.email(), clientRequest.phoneNumber(), clientRequest.address());

        Client saved = clientRepository.save(client);
        log.info("Client created with ID: {}", saved.getId());
        return clientMapper.toResponse(saved);
    }

    public ClientResponse getDetails(Long id) {
        Client client = clientRepository.findById(id).orElseThrow(() -> new NotFoundException("Client with id " + id));
        return clientMapper.toResponse(client);
    }


    public Page<ClientResponse> searchClient(String name, String identificationNumber, Pageable pageable) {
        if (identificationNumber != null && !identificationNumber.isBlank()) {
            var clientOpt = clientRepository.findByIdentificationNumber(identificationNumber);
            if (clientOpt.isPresent()) {
                return new PageImpl<>(List.of(clientMapper.toResponse(clientOpt.get())), pageable, 1);
            }
            return Page.empty(pageable);
        }

        String safeName = (name == null) ? "" : name.trim();
        if (!safeName.isEmpty()) {
            return clientRepository.findByNameContainingIgnoreCase(safeName, pageable)
                    .map(clientMapper::toResponse);
        }

        return clientRepository.findAll(pageable).map(clientMapper::toResponse);
    }

    @Transactional
    public ClientResponse updateClient(Long id, ClientRequest clientRequest) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Client with id " + id));

        client.setName(clientRequest.name().trim());
        client.setAddress(clientRequest.address() == null ? null : clientRequest.address().trim());
        client.setEmail(clientRequest.email().trim());
        client.setPhoneNumber(clientRequest.phoneNumber().trim());
        client.setType(clientRequest.type());

        String newIdNumber = clientRequest.identificationNumber().trim();
        String oldIdNumber = client.getIdentificationNumber();

        if (!newIdNumber.equals(oldIdNumber)) {

            if (client.getNumberOfChangesOfIn() >= 3) {
                throw new IllegalArgumentException("Maximum id changes reached.");
            }

            if (clientRepository.existsByIdentificationNumber(newIdNumber)) {
                throw new NotUniqueException("identification number");
            }

            client.setIdentificationNumber(newIdNumber);
            client.setNumberOfChangesOfIn(client.getNumberOfChangesOfIn() + 1);
            log.info("Identification number updated for client with ID: {}", client.getId());
        }

        Client saved = clientRepository.save(client);
        log.info("Client updated with ID: {}", saved.getId());
        return clientMapper.toResponse(saved);
    }
}
