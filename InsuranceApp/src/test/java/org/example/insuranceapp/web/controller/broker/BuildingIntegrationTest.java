package org.example.insuranceapp.web.controller.broker;

import org.example.insuranceapp.domain.building.Building;
import org.example.insuranceapp.domain.building.BuildingRepository;
import org.example.insuranceapp.domain.building.RiskIndicator;
import org.example.insuranceapp.domain.client.ClientRepository;
import org.example.insuranceapp.domain.geography.city.City;
import org.example.insuranceapp.domain.geography.city.CityRepository;
import org.example.insuranceapp.domain.geography.country.Country;
import org.example.insuranceapp.domain.geography.country.CountryRepository;
import org.example.insuranceapp.domain.geography.county.County;
import org.example.insuranceapp.domain.geography.county.CountyRepository;
import tools.jackson.databind.ObjectMapper;
import org.example.insuranceapp.domain.building.BuildingType;
import org.example.insuranceapp.domain.client.Client;
import org.example.insuranceapp.domain.client.ClientType;
import org.example.insuranceapp.web.dto.building.BuildingRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BuildingIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Autowired private ClientRepository clientRepository;
    @Autowired private BuildingRepository buildingRepository;
    @Autowired private CountryRepository countryRepository;
    @Autowired private CountyRepository countyRepository;
    @Autowired private CityRepository cityRepository;

    private Long savedClientId;
    private Long savedCityId;

    @BeforeEach
    void setUp() {
        Country country = new Country("Test Country");
        country = countryRepository.save(country);

        County county = countyRepository.save(new County("Test County", country));
        City city = cityRepository.save(new City("Test City", county));
        savedCityId = city.getId();

        Client client = new Client(ClientType.INDIVIDUAL, "Test Client", "9998887776665", "test@client.com", "0722111222", "Adresa Test");
        client = clientRepository.save(client);
        savedClientId = client.getId();
    }

    @Test
    void createBuilding() throws Exception {
        BuildingRequest request = new BuildingRequest(
                "Strada Testului",
                45,
                savedCityId,
                2022,
                BuildingType.RESIDENTIAL,
                2,
                150L,
                new BigDecimal("200000.00"),
                RiskIndicator.FLOOD
        );

        String response = mockMvc.perform(post("/api/brokers/clients/" + savedClientId +"/buildings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.street").value("Strada Testului"))
                .andExpect(jsonPath("$.clientId").value(savedClientId))
                .andExpect(jsonPath("$.geography.city").value("Test City"))
                .andReturn().getResponse().getContentAsString();

        Long buildingId = objectMapper.readTree(response).get("id").asLong();

        Building saved = buildingRepository.findById(buildingId).orElseThrow();
        assertEquals("Strada Testului", saved.getStreet());
        assertEquals(savedClientId, saved.getClient().getId());
        assertEquals(savedCityId, saved.getCity().getId());
    }

    @Test
    void createBuilding_FailClientNotFound() throws Exception {
        long before = buildingRepository.count();

        BuildingRequest request = new BuildingRequest(
                "Strada Fantoma", 1, savedCityId, 2020, BuildingType.OFFICE, 1, 50L, BigDecimal.TEN, RiskIndicator.NONE
        );

        mockMvc.perform(post("/api/brokers/clients/99999/buildings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
        assertEquals(before, buildingRepository.count());
    }

    @Test
    void createBuilding_Fail_InsuredValueIsNegative() throws Exception {
        long before = buildingRepository.count();

        BuildingRequest request = new BuildingRequest(
                "Street",
                1,
                savedCityId,
                2022,
                BuildingType.RESIDENTIAL,
                2,
                100L,
                new BigDecimal("-50.00"),
                RiskIndicator.FLOOD
        );

        mockMvc.perform(post("/api/brokers/clients/" + savedClientId + "/buildings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
        assertEquals(before, buildingRepository.count());
    }

    @Test
    void getBuildingsForClient() throws Exception {
        BuildingRequest request = new BuildingRequest(
                "Strada Existenta", 10, savedCityId, 2010, BuildingType.OFFICE, 1, 200L, BigDecimal.valueOf(10000), RiskIndicator.NONE
        );

        mockMvc.perform(post("/api/brokers/clients/" + savedClientId + "/buildings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/brokers/clients/" + savedClientId + "/buildings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].street").value("Strada Existenta"));
    }

    @Test
    void updateBuilding() throws Exception {
        BuildingRequest createReq = new BuildingRequest(
                "Strada Veche", 1, savedCityId, 2000, BuildingType.RESIDENTIAL, 1, 100L, BigDecimal.TEN, RiskIndicator.NONE
        );

        String responseJson = mockMvc.perform(post("/api/brokers/clients/" + savedClientId + "/buildings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andReturn().getResponse().getContentAsString();

        Long buildingId = objectMapper.readTree(responseJson).get("id").asLong();

        BuildingRequest updateReq = new BuildingRequest(
                "Strada Noua", 99, savedCityId, 2000, BuildingType.RESIDENTIAL, 1, 100L, BigDecimal.TEN, RiskIndicator.NONE
        );

        mockMvc.perform(put("/api/brokers/buildings/" + buildingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.street").value("Strada Noua"))
                .andExpect(jsonPath("$.number").value(99));

        Building updated = buildingRepository.findById(buildingId).orElseThrow();
        assertEquals("Strada Noua", updated.getStreet());
        assertEquals(99, updated.getNumber());

    }

    @Test
    void getBuildingDetails() throws Exception {
        BuildingRequest createReq = new BuildingRequest(
                "Strada Detaliilor",
                15,
                savedCityId,
                2021,
                BuildingType.OFFICE,
                3,
                500L,
                new BigDecimal("450000.00"),
                RiskIndicator.NONE
        );

        String responseJson = mockMvc.perform(post("/api/brokers/clients/" + savedClientId + "/buildings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long buildingId = objectMapper.readTree(responseJson).get("id").asLong();

        mockMvc.perform(get("/api/brokers/buildings/" + buildingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(buildingId))
                .andExpect(jsonPath("$.street").value("Strada Detaliilor"))
                .andExpect(jsonPath("$.clientId").value(savedClientId))
                .andExpect(jsonPath("$.geography.city").value("Test City"))
                .andExpect(jsonPath("$.geography.county").value("Test County"));
    }

    @Test
    void getBuildingDetails_FailBuildingNotFound() throws Exception {
        mockMvc.perform(get("/api/brokers/buildings/99999"))
                .andExpect(status().isNotFound());
    }
}