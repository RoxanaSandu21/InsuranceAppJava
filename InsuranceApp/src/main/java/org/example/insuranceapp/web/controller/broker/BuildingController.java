package org.example.insuranceapp.web.controller.broker;

import jakarta.validation.Valid;
import org.example.insuranceapp.application.service.BuildingService;
import org.example.insuranceapp.web.dto.building.BuildingRequest;
import org.example.insuranceapp.web.dto.building.BuildingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/brokers")
public class BuildingController {
    private final BuildingService buildingService;

    public BuildingController(BuildingService buildingService){
        this.buildingService = buildingService;
    }

    @PostMapping("/clients/{clientId}/buildings")
    public ResponseEntity<BuildingResponse> registerBuilding(@PathVariable Long clientId, @RequestBody @Valid BuildingRequest buildingRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(buildingService.registerBuilding(clientId, buildingRequest));
    }

    @PutMapping("/buildings/{buildingId}")
    public BuildingResponse updateBuilding(@PathVariable Long buildingId, @RequestBody @Valid BuildingRequest buildingRequest){
        return buildingService.updateBuilding(buildingId, buildingRequest);
    }

    @GetMapping("/buildings/{buildingId}")
    public BuildingResponse getDetails(@PathVariable Long buildingId) {
        return buildingService.getDetails(buildingId);
    }

    @GetMapping("/clients/{clientId}/buildings")
    public Page<BuildingResponse> getBuildingsForClient(@PathVariable Long clientId, Pageable pageable){
        return buildingService.getBuildingsForClient(clientId, pageable);
    }
}
