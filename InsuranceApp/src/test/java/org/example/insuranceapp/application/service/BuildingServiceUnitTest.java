package org.example.insuranceapp.application.service;

import org.example.insuranceapp.application.exception.NotFoundException;
import org.example.insuranceapp.domain.building.BuildingRepository;
import org.example.insuranceapp.domain.client.ClientRepository;
import org.example.insuranceapp.domain.geography.city.CityRepository;
import org.example.insuranceapp.web.mapper.BuildingMapper;
import org.example.insuranceapp.domain.building.Building;
import org.example.insuranceapp.domain.building.BuildingType;
import org.example.insuranceapp.domain.building.RiskIndicator;
import org.example.insuranceapp.domain.client.Client;
import org.example.insuranceapp.domain.geography.city.City;
import org.example.insuranceapp.domain.geography.country.Country;
import org.example.insuranceapp.domain.geography.county.County;
import org.example.insuranceapp.web.dto.building.BuildingRequest;
import org.example.insuranceapp.web.dto.building.BuildingResponse;
import org.example.insuranceapp.web.dto.geography.GeographyResponse;
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
class BuildingServiceUnitTest {

    @Mock private BuildingRepository buildingRepository;
    @Mock private ClientRepository clientRepository;
    @Mock private CityRepository cityRepository;

    @Mock private BuildingMapper buildingMapper;

    @InjectMocks private BuildingService buildingService;

    private Client mockClient;
    private City mockCity;
    private Building mockBuilding;

    @BeforeEach
    void setUp() {
        mockClient = new Client();
        mockClient.setId(1L);

        Country country = new Country("Romania");
        country.setId(10L);
        County county = new County("Bucuresti", country);
        county.setId(20L);
        mockCity = new City("Sector 1", county);
        mockCity.setId(30L);

        mockBuilding = new Building(mockClient, "Street", 1, mockCity, 2020, BuildingType.RESIDENTIAL, 1, 100L, BigDecimal.TEN, RiskIndicator.FLOOD);
        mockBuilding.setId(100L);
    }

    @Test
    void registerBuilding_Success() {
        BuildingRequest req = new BuildingRequest( "Street", 1, 30L, 2020, BuildingType.RESIDENTIAL, 1, 100L, BigDecimal.TEN, RiskIndicator.FLOOD);
        GeographyResponse geo = new GeographyResponse(30L, "Sector 1", 20L, "Bucuresti", 10L, "Romania");
        BuildingResponse mockResponse = new BuildingResponse(100L, 1L, "Street", 1, 2020, BuildingType.RESIDENTIAL, 1, 100L, BigDecimal.TEN, null, geo, List.of());

        when(clientRepository.findById(1L)).thenReturn(Optional.of(mockClient));
        when(cityRepository.findById(30L)).thenReturn(Optional.of(mockCity));
        when(buildingRepository.save(any(Building.class))).thenReturn(mockBuilding);
        when(buildingMapper.toResponse(any(Building.class))).thenReturn(mockResponse);

        BuildingResponse res = buildingService.registerBuilding(1L, req);

        assertNotNull(res);
        assertNotNull(res.geography());
        assertEquals(100L, res.id());
        assertEquals("Sector 1", res.geography().city());
    }

    @Test
    void registerBuilding_ClientNotFound() {
        BuildingRequest req = new BuildingRequest("Street", 1, 30L, 2020, BuildingType.RESIDENTIAL, 1, 100L, BigDecimal.TEN, RiskIndicator.FLOOD);
        when(clientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> buildingService.registerBuilding(99L, req));
    }

    @Test
    void registerBuilding_CityNotFound() {
        BuildingRequest req = new BuildingRequest("Street", 1, 99L, 2020, BuildingType.RESIDENTIAL, 1, 100L, BigDecimal.TEN, RiskIndicator.FLOOD);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(mockClient));
        when(cityRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> buildingService.registerBuilding(1L, req));
    }

    @Test
    void updateBuilding_Success() {
        BuildingRequest req = new BuildingRequest( "New Street", 2, 30L, 2021, BuildingType.OFFICE, 2, 200L, BigDecimal.TEN, RiskIndicator.FLOOD);
        BuildingResponse mockResponse = new BuildingResponse(100L, 1L, "New Street", 2, 2021, BuildingType.OFFICE, 2, 200L, BigDecimal.TEN, RiskIndicator.FLOOD, null, List.of());

        when(buildingRepository.findById(100L)).thenReturn(Optional.of(mockBuilding));
        when(cityRepository.findById(30L)).thenReturn(Optional.of(mockCity));
        when(buildingRepository.save(any(Building.class))).thenAnswer(inv -> inv.getArgument(0));
        when(buildingMapper.toResponse(any(Building.class))).thenReturn(mockResponse);

        BuildingResponse res = buildingService.updateBuilding(100L, req);

        assertEquals("New Street", res.street());
        assertEquals(2, res.numberOfFloors());
    }

    @Test
    void getDetails_Success() {
        BuildingResponse mockResponse = new BuildingResponse(100L, 1L, "Street", 1, 2020, BuildingType.RESIDENTIAL, 1, 100L, BigDecimal.TEN, null, null, List.of());
        when(buildingRepository.findById(100L)).thenReturn(Optional.of(mockBuilding));
        when(buildingMapper.toResponse(any(Building.class))).thenReturn(mockResponse);
        BuildingResponse res = buildingService.getDetails(100L);
        assertEquals(100L, res.id());
    }

    @Test
    void getBuildingsForClient_Success() {
        BuildingResponse mockResponse = new BuildingResponse(100L, 1L, "Street", 1, 2020, BuildingType.RESIDENTIAL, 1, 100L, BigDecimal.TEN, null, null, List.of());

        when(clientRepository.existsById(1L)).thenReturn(true);
        when(buildingRepository.findByClientId(eq(1L), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(mockBuilding)));
        when(buildingMapper.toResponse(any(Building.class))).thenReturn(mockResponse);

        Page<BuildingResponse> page = buildingService.getBuildingsForClient(1L, Pageable.unpaged());

        assertEquals(1, page.getTotalElements());
        assertEquals(100L, page.getContent().get(0).id());
    }

    @Test
    void getBuildingsForClient_ClientNotFound() {
        Pageable pageable = Pageable.unpaged();
        when(clientRepository.existsById(99L)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> buildingService.getBuildingsForClient(99L, pageable));
    }
}