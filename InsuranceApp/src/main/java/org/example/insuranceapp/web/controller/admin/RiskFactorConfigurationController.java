package org.example.insuranceapp.web.controller.admin;

import jakarta.validation.Valid;
import org.example.insuranceapp.application.service.RiskFactorConfigurationService;
import org.example.insuranceapp.web.dto.metadata.RiskFactorConfigurationRequest;
import org.example.insuranceapp.web.dto.metadata.RiskFactorConfigurationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/risk-factors")
public class RiskFactorConfigurationController {
    private final RiskFactorConfigurationService riskFactorConfigurationService;

    public RiskFactorConfigurationController(RiskFactorConfigurationService riskFactorConfigurationService){
        this.riskFactorConfigurationService = riskFactorConfigurationService;
    }

    @GetMapping
    public Page<RiskFactorConfigurationResponse> getRiskFactors(Pageable pageable){
        return riskFactorConfigurationService.getRiskFactors(pageable);
    }

    @PostMapping
    public ResponseEntity<RiskFactorConfigurationResponse> createRiskFactor(@RequestBody @Valid RiskFactorConfigurationRequest riskFactorConfigurationRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(riskFactorConfigurationService.createRiskFactor(riskFactorConfigurationRequest));
    }

    @PutMapping("/{riskId}")
    public ResponseEntity<RiskFactorConfigurationResponse> updateRiskFactor(@PathVariable Long riskId, @RequestBody @Valid RiskFactorConfigurationRequest riskFactorConfigurationRequest){
        return ResponseEntity.status(HttpStatus.OK).body(riskFactorConfigurationService.updateRiskFactor(riskId, riskFactorConfigurationRequest));
    }

}
