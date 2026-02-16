package org.example.insuranceapp.web.controller.admin;

import org.example.insuranceapp.domain.metadata.risk.RiskFactorConfiguration;
import org.example.insuranceapp.domain.metadata.risk.RiskLevel;
import org.example.insuranceapp.infrastructure.persistence.repository.JpaRiskFactorConfigurationRepository;
import org.example.insuranceapp.web.dto.metadata.RiskFactorConfigurationRequest;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(properties = {
        "spring.sql.init.mode=never",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class RiskConfigurationIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private JpaRiskFactorConfigurationRepository riskRepository;

    @BeforeEach
    void setUp() {
        riskRepository.deleteAll();
    }

    @Test
    void createRiskFactor_Success() throws Exception {
        RiskFactorConfigurationRequest request = new RiskFactorConfigurationRequest(
                RiskLevel.CITY,
                1L,
                new BigDecimal("5.50"),
                true
        );

        mockMvc.perform(post("/api/admin/risk-factors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.level").value("CITY"))
                .andExpect(jsonPath("$.adjustmentPercentage").value(5.50))
                .andExpect(jsonPath("$.active").value(true));

        assertEquals(1, riskRepository.count());
    }

    @Test
    void createRiskFactor_Fail_DuplicateActiveConfig() throws Exception {
        riskRepository.save(new RiskFactorConfiguration(RiskLevel.CITY, 1L, BigDecimal.TEN, true));

        RiskFactorConfigurationRequest request = new RiskFactorConfigurationRequest(
                RiskLevel.CITY, 1L, new BigDecimal("2.0"), true
        );

        mockMvc.perform(post("/api/admin/risk-factors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void updateRiskFactor_Success() throws Exception {
        RiskFactorConfiguration saved = riskRepository.save(
                new RiskFactorConfiguration(RiskLevel.COUNTY, 2L, BigDecimal.ONE, true));

        RiskFactorConfigurationRequest updateRequest = new RiskFactorConfigurationRequest(
                RiskLevel.COUNTY, 2L, new BigDecimal("3.75"), true
        );

        mockMvc.perform(put("/api/admin/risk-factors/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.adjustmentPercentage").value(3.75));

        RiskFactorConfiguration updated = riskRepository.findById(saved.getId()).orElseThrow();
        assertEquals(0, updated.getAdjustmentPercentage().compareTo(new BigDecimal("3.75")));
    }

    @Test
    void getRiskFactors_Pagination() throws Exception {
        riskRepository.save(new RiskFactorConfiguration(RiskLevel.CITY, 1L, BigDecimal.ONE, true));
        riskRepository.save(new RiskFactorConfiguration(RiskLevel.CITY, 2L, BigDecimal.TEN, true));

        mockMvc.perform(get("/api/admin/risk-factors")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));
    }
}