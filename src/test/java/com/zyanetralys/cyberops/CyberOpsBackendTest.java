package com.zyanetralys.cyberops;

import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
class CyberOpsBackendTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void connectEndpointReturnsDemoSession() throws Exception {
        mockMvc.perform(get("/api/connect"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionId").exists())
                .andExpect(jsonPath("$.status").value("connected"));
    }
    @Test
    void connectEndpointReturnsUniqueUuidSessionIds() throws Exception {
        String first = mockMvc.perform(get("/api/connect"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        String second = mockMvc.perform(get("/api/connect"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String firstId = objectMapper.readTree(first).get("sessionId").asText();
        String secondId = objectMapper.readTree(second).get("sessionId").asText();

        assertDoesNotThrow(() -> UUID.fromString(firstId));
        assertDoesNotThrow(() -> UUID.fromString(secondId));
        assertNotEquals(firstId, secondId);
    }

    @Test
    void corsAllowsOnlyConfiguredLocalOrigin() throws Exception {
        mockMvc.perform(get("/api/connect").header("Origin", "http://localhost:5500"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:5500"))
                .andExpect(header().doesNotExist("Access-Control-Allow-Credentials"));

        mockMvc.perform(get("/api/connect").header("Origin", "https://untrusted.example"))
                .andExpect(status().isOk())
                .andExpect(header().doesNotExist("Access-Control-Allow-Origin"));
    }

    @Test
    void streamRejectsMalformedSessionIdentifiers() throws Exception {
        mockMvc.perform(get("/api/stream/not_valid!"))
                .andExpect(status().isBadRequest());
    }

}
