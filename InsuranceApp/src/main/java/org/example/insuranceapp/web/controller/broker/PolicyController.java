package org.example.insuranceapp.web.controller.broker;

import jakarta.validation.Valid;
import org.example.insuranceapp.application.service.PolicyService;
import org.example.insuranceapp.domain.policy.PolicyStatus;
import org.example.insuranceapp.web.dto.policy.PolicyDetails;
import org.example.insuranceapp.web.dto.policy.PolicyRequest;
import org.example.insuranceapp.web.dto.policy.PolicyResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/brokers/policies")
public class PolicyController {
    private final PolicyService policyService;

    public PolicyController(PolicyService policyService){
        this.policyService = policyService;
    }

    @GetMapping
    public Page<PolicyResponse> searchPolicies(@RequestParam(required = false) Long clientId, @RequestParam(required = false) Long brokerId, @RequestParam(required = false) PolicyStatus status, @RequestParam(required = false) LocalDate startDate, @RequestParam(required = false) LocalDate endDate, Pageable pageable){
        return policyService.searchPolicies(clientId, brokerId, status, startDate, endDate, pageable);
    }

    @GetMapping("/{policyId}")
    public PolicyDetails viewDetails(@PathVariable Long policyId){
        return policyService.viewDetails(policyId);
    }

    @PostMapping
    public ResponseEntity<PolicyResponse> createDraft(@RequestBody @Valid PolicyRequest policyRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(policyService.createDraft(policyRequest));
    }

    @PostMapping("/{policyId}/activate")
    public ResponseEntity<PolicyResponse> activatePolicy(@PathVariable Long policyId){
        return ResponseEntity.status(HttpStatus.OK).body(policyService.activatePolicy(policyId));
    }

    @PostMapping("/{policyId}/cancel")
    public ResponseEntity<PolicyResponse> cancelPolicy(@PathVariable Long policyId, @RequestBody String cancellationReason){
        return ResponseEntity.status(HttpStatus.OK).body(policyService.cancelPolicy(policyId, cancellationReason));
    }


}
