package org.example.insuranceapp.web.controller.admin;

import org.example.insuranceapp.domain.metadata.fee.FeeConfiguration;
import org.example.insuranceapp.domain.metadata.fee.FeeType;
import org.example.insuranceapp.infrastructure.persistence.repository.JpaFeeConfigurationRepository;
import org.example.insuranceapp.web.dto.metadata.FeeConfigurationRequest;
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
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(properties = {
        "spring.sql.init.mode=never",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class FeeConfigurationIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private JpaFeeConfigurationRepository feeRepository;

    @BeforeEach
    void setUp() {
        feeRepository.deleteAll();
    }

    @Test
    void createFee_Success() throws Exception {
        FeeConfigurationRequest request = new FeeConfigurationRequest(
                "Standard Admin Fee",
                FeeType.ADMIN_FEE,
                new BigDecimal("2.50"),
                LocalDate.now(),
                LocalDate.now().plusYears(1),
                true
        );

        mockMvc.perform(post("/api/admin/fees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Standard Admin Fee"))
                .andExpect(jsonPath("$.percentage").value(2.50))
                .andExpect(jsonPath("$.type").value("ADMIN_FEE"));

        assertEquals(1, feeRepository.count());
    }

    @Test
    void createFee_Fail_InvalidDates() throws Exception {
        FeeConfigurationRequest request = new FeeConfigurationRequest(
                "Invalid Dates Fee",
                FeeType.ADMIN_FEE,
                BigDecimal.ONE,
                LocalDate.now().plusDays(10),
                LocalDate.now().plusDays(5),
                true
        );

        mockMvc.perform(post("/api/admin/fees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateFee_Success() throws Exception {
        FeeConfiguration saved = feeRepository.save(new FeeConfiguration(
                "Old Name", FeeType.BROKER_COMMISSION, BigDecimal.TEN, LocalDate.now(), null, true));

        FeeConfigurationRequest updateRequest = new FeeConfigurationRequest(
                "New Name",
                FeeType.BROKER_COMMISSION,
                new BigDecimal("15.00"),
                LocalDate.now(),
                null,
                true
        );

        mockMvc.perform(put("/api/admin/fees/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Name"))
                .andExpect(jsonPath("$.percentage").value(15.00));

        FeeConfiguration updated = feeRepository.findById(saved.getId()).orElseThrow();
        assertEquals("New Name", updated.getName());
        assertEquals(new BigDecimal("15.00"), updated.getPercentage());
    }

    @Test
    void getFees_Pagination() throws Exception {
        feeRepository.save(new FeeConfiguration("Fee 1", FeeType.ADMIN_FEE, BigDecimal.ONE, null, null, true));
        feeRepository.save(new FeeConfiguration("Fee 2", FeeType.ADMIN_FEE, BigDecimal.TEN, null, null, true));

        mockMvc.perform(get("/api/admin/fees")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    void updateFee_NotFound() throws Exception {
        FeeConfigurationRequest request = new FeeConfigurationRequest("Test", FeeType.ADMIN_FEE, BigDecimal.ONE, null, null, true);

        mockMvc.perform(put("/api/admin/fees/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}