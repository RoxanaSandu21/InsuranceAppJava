package org.example.insuranceapp.web.controller.admin;

import org.example.insuranceapp.domain.broker.Broker;
import org.example.insuranceapp.domain.building.Building;
import org.example.insuranceapp.domain.building.BuildingType;
import org.example.insuranceapp.domain.building.RiskIndicator;
import org.example.insuranceapp.domain.client.Client;
import org.example.insuranceapp.domain.client.ClientType;
import org.example.insuranceapp.domain.geography.city.City;
import org.example.insuranceapp.domain.geography.country.Country;
import org.example.insuranceapp.domain.geography.county.County;
import org.example.insuranceapp.domain.metadata.currency.Currency;
import org.example.insuranceapp.domain.policy.Policy;
import org.example.insuranceapp.domain.policy.PolicyStatus;
import org.example.insuranceapp.infrastructure.persistence.ClientRepositoryAdapter;
import org.example.insuranceapp.infrastructure.persistence.repository.*;
import org.example.insuranceapp.web.dto.metadata.CurrencyRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(properties = {
        "spring.sql.init.mode=never",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class CurrencyIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private JpaCurrencyRepository currencyRepository;
    @Autowired private JpaPolicyRepository policyRepository;
    @Autowired private ClientRepositoryAdapter clientRepositoryAdapter;
    @Autowired private JpaBuildingRepository buildingRepository;
    @Autowired private JpaBrokerRepository brokerRepository;
    @Autowired private JpaCityRepository cityRepository;
    @Autowired private JpaCountyRepository countyRepository;
    @Autowired private JpaCountryRepository countryRepository;

    @BeforeEach
    void setUp() {
        policyRepository.deleteAll();
        buildingRepository.deleteAll();
        clientRepositoryAdapter.deleteAll();
        brokerRepository.deleteAll();
        currencyRepository.deleteAll();
        cityRepository.deleteAll();
        countyRepository.deleteAll();
        countryRepository.deleteAll();
    }

    @Test
    void createCurrency_Success() throws Exception {
        CurrencyRequest request = new CurrencyRequest("EUR", "Euro", new BigDecimal("4.97"), true);

        mockMvc.perform(post("/api/admin/currencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("EUR"))
                .andExpect(jsonPath("$.exchangeRateToBase").value(4.97));
    }

    @Test
    void createCurrency_Fail_DuplicateCode() throws Exception {
        currencyRepository.save(new Currency("RON", "Leu", BigDecimal.ONE, true));
        CurrencyRequest request = new CurrencyRequest("RON", "Alt Leu", BigDecimal.ONE, true);

        mockMvc.perform(post("/api/admin/currencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void updateCurrencyStatus_Deactivate_Success_NoPolicies() throws Exception {
        Currency ron = currencyRepository.save(new Currency("RON", "Leu", BigDecimal.ONE, true));

        mockMvc.perform(patch("/api/admin/currencies/" + ron.getId())
                        .param("active", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(false));
    }

    @Test
    void updateCurrencyStatus_Deactivate_Fail_HasActivePolicies() throws Exception {
        Currency ron = currencyRepository.save(new Currency("RON", "Leu", BigDecimal.ONE, true));
        Country country = countryRepository.save(new Country("Romania"));
        County county = countyRepository.save(new County("Bucuresti", country));
        City city = cityRepository.save(new City("Bucuresti", county));
        Client client = clientRepositoryAdapter.save(new Client(ClientType.INDIVIDUAL, "Ion", "123", "i@t.com", "07", "A"));
        Building building = buildingRepository.save(new Building(client, "Strada", 1, city, 2020, BuildingType.RESIDENTIAL, 1, 100L, BigDecimal.TEN, RiskIndicator.NONE));
        Broker broker = brokerRepository.save(new Broker("B01", "Broker", "b@t.com", "07", true, BigDecimal.ONE));

        Policy policy = new Policy();
        policy.setClient(client);
        policy.setBuilding(building);
        policy.setBroker(broker);
        policy.setCurrency(ron);
        policy.setPolicyStatus(PolicyStatus.ACTIVE);
        policy.setStartDate(LocalDate.now());
        policy.setEndDate(LocalDate.now().plusYears(1));
        policy.setBasePremium(BigDecimal.TEN);
        policy.setFinalPremium(BigDecimal.TEN);
        policyRepository.save(policy);

        mockMvc.perform(patch("/api/admin/currencies/" + ron.getId())
                        .param("active", "false"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllCurrencies_Pagination() throws Exception {
        currencyRepository.save(new Currency("USD", "Dolar", new BigDecimal("4.5"), true));
        currencyRepository.save(new Currency("GBP", "Lira", new BigDecimal("5.8"), true));

        mockMvc.perform(get("/api/admin/currencies")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));
    }
}