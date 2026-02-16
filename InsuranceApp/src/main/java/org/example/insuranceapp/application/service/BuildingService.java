package org.example.insuranceapp.application.service;

import jakarta.transaction.Transactional;
import org.example.insuranceapp.domain.building.BuildingRepository;
import org.example.insuranceapp.domain.geography.city.CityRepository;
import org.example.insuranceapp.web.mapper.BuildingMapper;
import org.example.insuranceapp.domain.building.Building;
import org.example.insuranceapp.domain.client.Client;
import org.example.insuranceapp.domain.geography.city.City;
import org.example.insuranceapp.infrastructure.persistence.ClientRepositoryAdapter;
import org.example.insuranceapp.web.dto.building.BuildingRequest;
import org.example.insuranceapp.application.exception.NotFoundException;
import org.example.insuranceapp.web.dto.building.BuildingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BuildingService {
    private final BuildingRepository buildingRepository;
    private final ClientRepositoryAdapter clientRepositoryAdapter;
    private final CityRepository cityRepository;
    private final BuildingMapper buildingMapper;

    private static final Logger log = LoggerFactory.getLogger(BuildingService.class);

    public BuildingService(BuildingRepository buildingRepository, ClientRepositoryAdapter clientRepositoryAdapter, CityRepository cityRepository, BuildingMapper buildingMapper){
        this.buildingRepository = buildingRepository;
        this.clientRepositoryAdapter = clientRepositoryAdapter;
        this.cityRepository = cityRepository;
        this.buildingMapper = buildingMapper;
    }

    public BuildingResponse registerBuilding(Long clientId, BuildingRequest buildingRequest){
        Client client = clientRepositoryAdapter.findById(clientId).orElseThrow(() -> new NotFoundException("Client with id " + clientId));
        City city = cityRepository.findById(buildingRequest.cityId()).orElseThrow(() -> new NotFoundException("City with id " + buildingRequest.cityId()));

        Building building = new Building(client, buildingRequest.street(), buildingRequest.number(), city, buildingRequest.constructionYear(), buildingRequest.type(), buildingRequest.numberOfFloors(), buildingRequest.surfaceArea(),
                buildingRequest.insuredValue(), buildingRequest.riskIndicator());

        Building saved = buildingRepository.save(building);
        log.info("Building created with ID: {}", saved.getId());
        return buildingMapper.toResponse(saved);
    }

    @Transactional
    public BuildingResponse updateBuilding(Long id, BuildingRequest buildingRequest){
        Building building = buildingRepository.findById(id).orElseThrow(() -> new NotFoundException("Building with id " + id));

        City city = cityRepository.findById(buildingRequest.cityId()).orElseThrow(() -> new NotFoundException("City with id " + buildingRequest.cityId()));
        building.setCity(city);
        building.setConstructionYear(buildingRequest.constructionYear());
        building.setNumber(buildingRequest.number());
        building.setInsuredValue(buildingRequest.insuredValue());
        building.setNumberOfFloors(buildingRequest.numberOfFloors());
        building.setRiskIndicator(buildingRequest.riskIndicator());
        building.setStreet(buildingRequest.street());
        building.setSurfaceArea(buildingRequest.surfaceArea());
        building.setType(buildingRequest.type());

        Building saved = buildingRepository.save(building);
        log.info("Building updated with ID: {}", saved.getId());
        return buildingMapper.toResponse(saved);
    }

    public BuildingResponse  getDetails(Long id){
        Building building = buildingRepository.findById(id).orElseThrow(() -> new NotFoundException("Building with id " + id));
        return buildingMapper.toResponse(building);
    }

    public Page<BuildingResponse> getBuildingsForClient(Long clientId, Pageable pageable){
        if (!clientRepositoryAdapter.existsById(clientId)) {
            throw new NotFoundException("Client with id " + clientId);
        }

        Page<Building> buildingsPage = buildingRepository.findByClientId(clientId, pageable);

        return buildingsPage.map(buildingMapper::toResponse);
    }
}
