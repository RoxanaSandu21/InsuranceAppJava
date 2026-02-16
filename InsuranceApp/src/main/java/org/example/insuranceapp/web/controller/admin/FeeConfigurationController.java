package org.example.insuranceapp.web.controller.admin;

import jakarta.validation.Valid;
import org.example.insuranceapp.application.service.FeeConfigurationService;
import org.example.insuranceapp.web.dto.metadata.FeeConfigurationRequest;
import org.example.insuranceapp.web.dto.metadata.FeeConfigurationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/fees")
public class FeeConfigurationController {
    private final FeeConfigurationService feeConfigurationService;

    public FeeConfigurationController(FeeConfigurationService feeConfigurationService){
        this.feeConfigurationService = feeConfigurationService;
    }

    @GetMapping
    public Page<FeeConfigurationResponse> getFees(Pageable pageable){
        return feeConfigurationService.getFees(pageable);
    }

    @PostMapping
    public ResponseEntity<FeeConfigurationResponse> createFee(@RequestBody @Valid FeeConfigurationRequest feeConfigurationRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(feeConfigurationService.createFee(feeConfigurationRequest));
    }

    @PutMapping("/{feeId}")
    public ResponseEntity<FeeConfigurationResponse> updateFee(@PathVariable Long feeId, @RequestBody @Valid FeeConfigurationRequest feeConfigurationRequest){
        return ResponseEntity.status(HttpStatus.OK).body(feeConfigurationService.updateFee(feeId, feeConfigurationRequest));
    }

}
