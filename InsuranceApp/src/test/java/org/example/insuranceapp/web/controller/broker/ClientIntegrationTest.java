package org.example.insuranceapp.web.controller.broker;

import org.example.insuranceapp.domain.building.BuildingRepository;
import org.example.insuranceapp.domain.client.Client;
import org.example.insuranceapp.domain.client.ClientRepository;
import org.example.insuranceapp.domain.policy.PolicyRepository;
import org.example.insuranceapp.web.dto.client.ClientRequest;
import tools.jackson.databind.ObjectMapper;
import org.example.insuranceapp.domain.client.ClientType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ClientIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private ClientRepository clientRepository;
    @Autowired private PolicyRepository policyRepository;
    @Autowired private BuildingRepository buildingRepository;

    @Test
    void clientFullFlowTestSuccess() throws Exception {
        ClientRequest request = new ClientRequest(ClientType.INDIVIDUAL, "Ion Popescu", "1900101123456", "ion@test.com", "0722000000", "Bucuresti");


        String response = mockMvc.perform(post("/api/brokers/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long clientId = objectMapper.readTree(response).get("id").asLong();

        Client created = clientRepository.findById(clientId).orElseThrow();
        assertEquals("Ion Popescu", created.getName());
        assertEquals("1900101123456", created.getIdentificationNumber());
        assertEquals("ion@test.com", created.getEmail());



        mockMvc.perform(get("/api/brokers/clients/" + clientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ion Popescu"));


        ClientRequest updateRequest = new ClientRequest(ClientType.INDIVIDUAL, "Ion Popescu Actualizat", "1900101123456", "ion@test.com", "0722000000", "Bucuresti");
        mockMvc.perform(put("/api/brokers/clients/" + clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ion Popescu Actualizat"));

        Client updated = clientRepository.findById(clientId).orElseThrow();
        assertEquals("Ion Popescu Actualizat", updated.getName());



        mockMvc.perform(get("/api/brokers/clients").param("name", "Actualizat"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(clientId));
    }

    @Test
    void getAllClients() throws Exception {
        policyRepository.deleteAll();
        buildingRepository.deleteAll();
        clientRepository.deleteAll();

        clientRepository.save(new Client(ClientType.INDIVIDUAL, "Alice", "100", "a@test.com", "0700", "Addr1"));
        clientRepository.save(new Client(ClientType.COMPANY, "Bob SRL", "200", "b@test.com", "0800", "Addr2"));

        mockMvc.perform(get("/api/brokers/clients")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "name,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].name").value("Alice"))
                .andExpect(jsonPath("$.content[1].name").value("Bob SRL"));
    }

    @Test
    void searchClient() throws Exception {
        String uniqueId = "999888777";
        clientRepository.save(new Client(ClientType.INDIVIDUAL, "Charlie", uniqueId, "c@test.com", "0900", "Addr3"));

        mockMvc.perform(get("/api/brokers/clients")
                        .param("identificationNumber", uniqueId)
                        .param("name", "")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Charlie"))
                .andExpect(jsonPath("$.content[0].identificationNumber").value(uniqueId));
    }

    @Test
    void createClient_FailWhenIdentificationNumberExists() throws Exception {
        ClientRequest request = new ClientRequest(ClientType.INDIVIDUAL, "Ion", "123", "a@a.com", "07", null);

        mockMvc.perform(post("/api/brokers/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        long before = clientRepository.count();

        mockMvc.perform(post("/api/brokers/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());

        assertEquals(before, clientRepository.count());
    }

    @Test
    void createClient_FailWhenNameIsEmpty() throws Exception {
        ClientRequest request = new ClientRequest(ClientType.INDIVIDUAL, "", "123456", "valid@email.com", "0722", "Adresa");

        mockMvc.perform(post("/api/brokers/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    void createClient_FailWhenEmailIsInvalid() throws Exception {
        ClientRequest request = new ClientRequest(ClientType.INDIVIDUAL, "Ion", "123456", "invalid-email", "0722", "Adresa");

        mockMvc.perform(post("/api/brokers/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}