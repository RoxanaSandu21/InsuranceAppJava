package org.example.insuranceapp.web.controller.admin;

import jakarta.validation.Valid;
import org.example.insuranceapp.application.service.BrokerService;
import org.example.insuranceapp.web.dto.broker.BrokerRequest;
import org.example.insuranceapp.web.dto.broker.BrokerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/brokers")
public class BrokerController {
    private final BrokerService brokerService;

    public BrokerController(BrokerService brokerService){
        this.brokerService = brokerService;
    }

    @GetMapping
    public Page<BrokerResponse> getAllBrokers(Pageable pageable){
        return brokerService.getAllBrokers(pageable);
    }

    @GetMapping("/{brokerId}")
    public BrokerResponse viewBrokerDetails(@PathVariable Long brokerId){
        return brokerService.viewBrokerDetails(brokerId);
    }

    @PostMapping
    public ResponseEntity<BrokerResponse> createBroker(@RequestBody @Valid BrokerRequest brokerRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(brokerService.createBroker(brokerRequest));
    }

    @PutMapping("/{brokerId}")
    public ResponseEntity<BrokerResponse> update(@PathVariable Long brokerId, @RequestBody @Valid BrokerRequest brokerRequest){
        return ResponseEntity.status(HttpStatus.OK).body(brokerService.update(brokerId, brokerRequest));
    }

    @PostMapping("/{brokerId}/activate")
    public ResponseEntity<BrokerResponse> activate(@PathVariable Long brokerId){
        return ResponseEntity.status(HttpStatus.OK).body(brokerService.activate(brokerId));
    }

    @PostMapping("/{brokerId}/deactivate")
    public ResponseEntity<BrokerResponse> deactivate(@PathVariable Long brokerId){
        return ResponseEntity.status(HttpStatus.OK).body(brokerService.deactivate(brokerId));
    }

}
