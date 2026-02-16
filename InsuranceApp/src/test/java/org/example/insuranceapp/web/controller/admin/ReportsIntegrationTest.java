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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(properties = {
        "spring.sql.init.mode=never",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class ReportsIntegrationTest {

    @Autowired private MockMvc mockMvc;


    @Autowired private JpaPolicyRepository policyRepository;
    @Autowired private ClientRepositoryAdapter clientRepositoryAdapter;
    @Autowired private JpaBrokerRepository brokerRepository;
    @Autowired private JpaBuildingRepository buildingRepository;
    @Autowired private JpaCountryRepository countryRepository;
    @Autowired private JpaCountyRepository countyRepository;
    @Autowired private JpaCityRepository cityRepository;
    @Autowired private JpaCurrencyRepository currencyRepository;

    private Currency ron;
    private Currency eur;
    private Broker broker1;
    private Broker broker2;

    @BeforeEach
    void setUp() {
        policyRepository.deleteAll();
        buildingRepository.deleteAll();
        brokerRepository.deleteAll();
        clientRepositoryAdapter.deleteAll();
        cityRepository.deleteAll();
        countyRepository.deleteAll();
        countryRepository.deleteAll();
        currencyRepository.deleteAll();

        seedData();
    }

    private void seedData() {

        ron = currencyRepository.save(new Currency("RON", "Leu", BigDecimal.ONE, true));
        eur = currencyRepository.save(new Currency("EUR", "Euro", new BigDecimal("5.00"), true));


        Country romania = countryRepository.save(new Country("Romania"));
        County bucuresti = countyRepository.save(new County("Bucuresti", romania));
        City sect1 = cityRepository.save(new City("Sector 1", bucuresti));

        Country uk = countryRepository.save(new Country("UK"));
        County londonC = countyRepository.save(new County("Greater London", uk));
        City london = cityRepository.save(new City("London", londonC));


        Client client = clientRepositoryAdapter.save(new Client(ClientType.INDIVIDUAL, "Ion", "123", "i@test.com", "07", "Adresa"));
        broker1 = brokerRepository.save(new Broker("B1", "Broker Alpha", "b1@t.com", "07", true, BigDecimal.TEN));
        broker2 = brokerRepository.save(new Broker("B2", "Broker Beta", "b2@t.com", "07", true, BigDecimal.TEN));


        Building houseRo = buildingRepository.save(new Building(client, "Strada 1", 1, sect1, 2000, BuildingType.RESIDENTIAL, 1, 100L, BigDecimal.TEN, RiskIndicator.NONE));
        Building officeUk = buildingRepository.save(new Building(client, "Street 2", 2, london, 2010, BuildingType.OFFICE, 5, 500L, BigDecimal.TEN, RiskIndicator.NONE));




        createPolicy(client, houseRo, broker1, ron, PolicyStatus.ACTIVE, new BigDecimal("100.00"), LocalDate.now());


        createPolicy(client, houseRo, broker1, ron, PolicyStatus.ACTIVE, new BigDecimal("200.00"), LocalDate.now());


        createPolicy(client, officeUk, broker2, eur, PolicyStatus.ACTIVE, new BigDecimal("100.00"), LocalDate.now());


        createPolicy(client, houseRo, broker1, ron, PolicyStatus.CANCELLED, new BigDecimal("500.00"), LocalDate.now());
    }

    @Test
    void reportByCountry_Success() throws Exception {


        mockMvc.perform(get("/api/admin/reports/policies-by-country"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))

                .andReturn();

        mockMvc.perform(get("/api/admin/reports/policies-by-country")
                        .param("status", "ACTIVE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))


                .andExpect(jsonPath("$[?(@.groupingKey == 'Romania')].currencyCode").value("RON"))
                .andExpect(jsonPath("$[?(@.groupingKey == 'Romania')].count").value(2))
                .andExpect(jsonPath("$[?(@.groupingKey == 'Romania')].totalFinalPremium").value(300.00))
                .andExpect(jsonPath("$[?(@.groupingKey == 'Romania')].totalFinalPremiumInBaseCurrency").value(300.00))


                .andExpect(jsonPath("$[?(@.groupingKey == 'UK')].currencyCode").value("EUR"))
                .andExpect(jsonPath("$[?(@.groupingKey == 'UK')].count").value(1))
                .andExpect(jsonPath("$[?(@.groupingKey == 'UK')].totalFinalPremium").value(100.00))
                .andExpect(jsonPath("$[?(@.groupingKey == 'UK')].totalFinalPremiumInBaseCurrency").value(500.00));
    }

    @Test
    void reportByBroker_WithDateFilter_Success() throws Exception {

        createPolicy(broker1.getId(), LocalDate.now().minusMonths(2));

        mockMvc.perform(get("/api/admin/reports/policies-by-broker")
                        .param("from", LocalDate.now().minusDays(5).toString())
                        .param("status", "ACTIVE"))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$[?(@.groupingKey == 'Broker Alpha')].count").value(2))
                .andExpect(jsonPath("$[?(@.groupingKey == 'Broker Alpha')].totalFinalPremium").value(300.00))

                .andExpect(jsonPath("$[?(@.groupingKey == 'Broker Beta')].count").value(1));
    }

    @Test
    void reportByCity_WithCurrencyFilter() throws Exception {

        mockMvc.perform(get("/api/admin/reports/policies-by-city")
                        .param("currencyId", eur.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].groupingKey").value("London"))
                .andExpect(jsonPath("$[0].currencyCode").value("EUR"));
    }

    @Test
    void reportByCounty_WithBuildingTypeFilter() throws Exception {

        mockMvc.perform(get("/api/admin/reports/policies-by-county")
                        .param("buildingType", "OFFICE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].groupingKey").value("Greater London"));
    }

    private void createPolicy(Client c, Building b, Broker br, Currency curr, PolicyStatus status, BigDecimal amount, LocalDate start) {
        Policy p = new Policy();
        p.setClient(c);
        p.setBuilding(b);
        p.setBroker(br);
        p.setCurrency(curr);
        p.setPolicyStatus(status);
        p.setFinalPremium(amount);
        p.setBasePremium(amount);
        p.setStartDate(start);
        p.setEndDate(start.plusYears(1));
        policyRepository.save(p);
    }


    private void createPolicy(Long brokerId, LocalDate startDate) {
        Broker b = brokerRepository.findById(brokerId).get();

        Building build = buildingRepository.findAll().get(0);
        Client client = clientRepositoryAdapter.findAll().get(0);

        createPolicy(client, build, b, ron, PolicyStatus.ACTIVE, BigDecimal.TEN, startDate);
    }
}