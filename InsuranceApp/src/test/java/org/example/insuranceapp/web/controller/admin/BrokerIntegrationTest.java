package org.example.insuranceapp.web.controller.admin;

import org.example.insuranceapp.domain.broker.Broker;
import org.example.insuranceapp.infrastructure.persistence.repository.JpaBrokerRepository;
import org.example.insuranceapp.infrastructure.persistence.repository.JpaPolicyRepository;
import org.example.insuranceapp.web.dto.broker.BrokerRequest;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(properties = {
        "spring.sql.init.mode=never",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class BrokerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private JpaBrokerRepository brokerRepository;
    @Autowired private JpaPolicyRepository policyRepository;

    @BeforeEach
    void setUp() {
        policyRepository.deleteAll();
        brokerRepository.deleteAll();
    }

    @Test
    void createBroker_Success() throws Exception {
        BrokerRequest request = new BrokerRequest(
                "BRK-TEST-01",
                "Broker de Integrare",
                "integrare@broker.ro",
                "0711222333",
                true,
                new BigDecimal("12.50")

        );

        mockMvc.perform(post("/api/admin/brokers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.brokerCode").value("BRK-TEST-01"))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.commissionPercentage").value(12.50));

        assertEquals(1, brokerRepository.count());
    }

    @Test
    void createBroker_Fail_DuplicateCode() throws Exception {
        Broker existing = new Broker("BRK-EXISTENT", "Nume", "email@test.ro", "07", true, BigDecimal.TEN);
        brokerRepository.save(existing);

        BrokerRequest request = new BrokerRequest("BRK-EXISTENT", "Alt Nume", "alt@email.ro", "07", true, BigDecimal.ZERO);

        mockMvc.perform(post("/api/admin/brokers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void updateBroker_Success() throws Exception {
        Broker saved = brokerRepository.save(new Broker("BRK-OLD", "Old Name", "old@test.ro", "07", true, BigDecimal.ONE));

        BrokerRequest updateRequest = new BrokerRequest("BRK-OLD", "New Name", "new@test.ro", "0744000111", true, BigDecimal.TEN);

        mockMvc.perform(put("/api/admin/brokers/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Name"))
                .andExpect(jsonPath("$.email").value("new@test.ro"));

        Broker updated = brokerRepository.findById(saved.getId()).orElseThrow();
        assertEquals("New Name", updated.getName());
    }

    @Test
    void deactivateBroker_Success() throws Exception {
        Broker activeBroker = brokerRepository.save(new Broker("BRK-ACT", "Active", "act@test.ro", "07", true, BigDecimal.TEN));

        mockMvc.perform(post("/api/admin/brokers/" + activeBroker.getId() + "/deactivate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(false));

        assertFalse(brokerRepository.findById(activeBroker.getId()).get().isActive());
    }

    @Test
    void activateBroker_Success() throws Exception {
        Broker inactiveBroker = new Broker("BRK-INACT", "Inactive", "inact@test.ro", "07", false, BigDecimal.TEN);
        inactiveBroker = brokerRepository.save(inactiveBroker);

        mockMvc.perform(post("/api/admin/brokers/" + inactiveBroker.getId() + "/activate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(true));

        assertTrue(brokerRepository.findById(inactiveBroker.getId()).get().isActive());
    }

    @Test
    void getBrokerDetails_NotFound() throws Exception {
        mockMvc.perform(get("/api/admin/brokers/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllBrokers_Pagination() throws Exception {
        brokerRepository.save(new Broker("B1", "Broker 1", "b1@test.ro", "07", true, BigDecimal.ONE));
        brokerRepository.save(new Broker("B2", "Broker 2", "b2@test.ro", "07", true, BigDecimal.ONE));

        mockMvc.perform(get("/api/admin/brokers")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));
    }
}