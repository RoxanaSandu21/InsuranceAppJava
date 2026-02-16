package org.example.insuranceapp.web.controller.broker;

import org.example.insuranceapp.domain.geography.city.City;
import org.example.insuranceapp.domain.geography.country.Country;
import org.example.insuranceapp.domain.geography.county.County;
import org.example.insuranceapp.infrastructure.persistence.repository.JpaCityRepository;
import org.example.insuranceapp.infrastructure.persistence.repository.JpaCountryRepository;
import org.example.insuranceapp.infrastructure.persistence.repository.JpaCountyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class GeographyIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private JpaCountryRepository countryRepository;
    @Autowired private JpaCountyRepository countyRepository;
    @Autowired private JpaCityRepository cityRepository;

    private Long savedCountryId;
    private Long savedCountyId;

    @BeforeEach
    void setUp() {
        Country country = new Country("Test Country");
        country = countryRepository.save(country);
        savedCountryId = country.getId();

        County county = new County("Test County", country);
        county = countyRepository.save(county);
        savedCountyId = county.getId();

        City city = new City("Test City", county);
        cityRepository.save(city);
    }

    @Test
    void getCountries() throws Exception {
        mockMvc.perform(get("/api/brokers/countries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[?(@.name == 'Test Country')]").exists());
    }

    @Test
    void getCounties() throws Exception {
        mockMvc.perform(get("/api/brokers/countries/" + savedCountryId + "/counties"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Test County"))
                .andExpect(jsonPath("$.content[0].countryId").value(savedCountryId));
    }

    @Test
    void getCities() throws Exception {
        mockMvc.perform(get("/api/brokers/counties/" + savedCountyId + "/cities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Test City"))
                .andExpect(jsonPath("$.content[0].countyId").value(savedCountyId));
    }

    @Test
    void shouldReturn404ForInvalidCountryId() throws Exception {
        mockMvc.perform(get("/api/brokers/countries/9999/counties"))
                .andExpect(status().isNotFound());
    }
}