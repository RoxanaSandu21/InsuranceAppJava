package org.example.insuranceapp.web.controller.broker;

import org.example.insuranceapp.application.service.PolicyService;
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
import org.example.insuranceapp.web.dto.policy.PolicyRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {
        "spring.sql.init.mode=never",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class PolicyIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JpaPolicyRepository policyRepository;
    @Autowired
    private ClientRepositoryAdapter clientRepositoryAdapter;
    @Autowired
    private JpaBrokerRepository brokerRepository;
    @Autowired
    private JpaBuildingRepository buildingRepository;
    @Autowired
    private JpaCityRepository cityRepository;
    @Autowired
    private JpaCountyRepository countyRepository;
    @Autowired
    private JpaCountryRepository countryRepository;
    @Autowired
    private JpaCurrencyRepository currencyRepository;
    @Autowired
    private PolicyService policyService;


    private Long savedClientId;
    private Long savedBuildingId;
    private Long savedBrokerId;
    private Long savedCurrencyId;

    @BeforeEach
    void setUp() {
        policyRepository.deleteAll();
        buildingRepository.deleteAll();
        clientRepositoryAdapter.deleteAll();
        brokerRepository.deleteAll();
        cityRepository.deleteAll();
        countyRepository.deleteAll();
        countryRepository.deleteAll();
        currencyRepository.deleteAll();

        Country country = countryRepository.save(new Country("Romania"));
        County county = countyRepository.save(new County("Bucuresti", country));
        City city = cityRepository.save(new City("Bucuresti", county));

        Client client = new Client(ClientType.INDIVIDUAL, "Ion Asiguratul", "1900101123456", "ion@test.com", "0722111222", "Adresa");
        client = clientRepositoryAdapter.save(client);
        savedClientId = client.getId();

        Building building = new Building(client, "Str. Victoriei", 10, city, 2010, BuildingType.RESIDENTIAL, 2, 100L, new BigDecimal("100000.00"), RiskIndicator.NONE);
        building = buildingRepository.save(building);
        savedBuildingId = building.getId();

        Broker broker = new Broker();
        broker.setBrokerCode("BRK-001");
        broker.setName("Broker Bun");
        broker.setEmail("broker@insurance.com");
        broker.setPhoneNumber("0733123456");
        broker.setUniqueIdentifier("UNIQUE-BRK-001");
        broker.setActive(true);
        broker.setCommissionPercentage(new BigDecimal("5.00"));
        broker = brokerRepository.save(broker);
        savedBrokerId = broker.getId();

        Currency currency = new Currency("RON", "Romanian Leu", BigDecimal.ONE, true);
        currency = currencyRepository.save(currency);
        savedCurrencyId = currency.getId();
    }

    @Test
    void createDraftPolicy_Success() throws Exception {
        PolicyRequest request = new PolicyRequest(
                savedClientId,
                savedBuildingId,
                savedBrokerId,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusYears(1),
                new BigDecimal("1000.00"),
                savedCurrencyId
        );

        String response = mockMvc.perform(post("/api/brokers/policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.policyStatus").value("DRAFT"))
                .andExpect(jsonPath("$.finalPremium").exists())
                .andReturn().getResponse().getContentAsString();

        Long policyId = objectMapper.readTree(response).get("id").asLong();
        assertTrue(policyRepository.existsById(policyId));
    }

    @Test
    void activatePolicy_Success() throws Exception {
        Policy draft = createTestPolicy(PolicyStatus.DRAFT, LocalDate.now().plusDays(1));

        mockMvc.perform(post("/api/brokers/policies/" + draft.getId() + "/activate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.policyStatus").value("ACTIVE"));

        assertEquals(PolicyStatus.ACTIVE, policyRepository.findById(draft.getId()).get().getPolicyStatus());
    }

    @Test
    void activatePolicy_Fail_StartDateInPast() throws Exception {
        Policy draft = createTestPolicy(PolicyStatus.DRAFT, LocalDate.now().minusDays(1));

        mockMvc.perform(post("/api/brokers/policies/" + draft.getId() + "/activate"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void cancelPolicy_Success() throws Exception {
        Policy activePolicy = createTestPolicy(PolicyStatus.ACTIVE, LocalDate.now());

        String reason = "Client requested cancellation";

        mockMvc.perform(post("/api/brokers/policies/" + activePolicy.getId() + "/cancel")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(reason))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.policyStatus").value("CANCELLED"))
                .andExpect(jsonPath("$.cancellationDate").exists());
    }

    @Test
    void searchPolicies_FilterByBroker() throws Exception {
        createTestPolicy(PolicyStatus.ACTIVE, LocalDate.now());

        mockMvc.perform(get("/api/brokers/policies")
                        .param("brokerId", savedBrokerId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].brokerId").value(savedBrokerId));
    }

    private Policy createTestPolicy(PolicyStatus status, LocalDate startDate) {
        Policy policy = new Policy();
        policy.setClient(clientRepositoryAdapter.findById(savedClientId).get());
        policy.setBuilding(buildingRepository.findById(savedBuildingId).get());
        policy.setBroker(brokerRepository.findById(savedBrokerId).get());
        policy.setCurrency(currencyRepository.findById(savedCurrencyId).get());
        policy.setPolicyStatus(status);
        policy.setStartDate(startDate);
        policy.setEndDate(startDate.plusYears(1));
        policy.setBasePremium(new BigDecimal("1000.00"));
        policy.setFinalPremium(new BigDecimal("1100.00"));
        return policyRepository.save(policy);
    }

    @Test
    void viewDetails_Success() throws Exception {
        Policy policy = createTestPolicy(PolicyStatus.ACTIVE, LocalDate.now());

        mockMvc.perform(get("/api/brokers/policies/" + policy.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(policy.getId()))
                .andExpect(jsonPath("$.policyNumber").value(policy.getPolicyNumber()));
    }

    @Test
    void viewDetails_NotFound() throws Exception {
        mockMvc.perform(get("/api/brokers/policies/99999"))
                .andExpect(status().isNotFound());
    }
}