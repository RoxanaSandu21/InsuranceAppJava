package org.example.insuranceapp.web.controller.broker;

import jakarta.validation.Valid;
import org.example.insuranceapp.application.service.ClientService;
import org.example.insuranceapp.web.dto.client.ClientRequest;
import org.example.insuranceapp.web.dto.client.ClientResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/brokers/clients")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService){
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<ClientResponse> createClient(@RequestBody @Valid ClientRequest clientRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.createClient(clientRequest));
    }

    @GetMapping("/{clientId}")
    public ClientResponse getDetails(@PathVariable Long clientId){
        return clientService.getDetails(clientId);
    }

    @GetMapping
    public Page<ClientResponse> searchClient(@RequestParam(required = false) String name, @RequestParam(required = false) String identificationNumber, Pageable pageable){
        return clientService.searchClient(name, identificationNumber, pageable);
    }

    @PutMapping("/{clientId}")
    public ClientResponse updateClient(@PathVariable Long clientId, @RequestBody @Valid ClientRequest clientRequest){
        return clientService.updateClient(clientId, clientRequest);
    }
}
