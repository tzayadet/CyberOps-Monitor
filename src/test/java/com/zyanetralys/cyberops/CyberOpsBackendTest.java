package com.zyanetralys.cyberops;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CyberOpsBackendTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void connectEndpointReturnsDemoSession() throws Exception {
        mockMvc.perform(get("/api/connect"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionId").value(org.hamcrest.Matchers.startsWith("zya-")))
                .andExpect(jsonPath("$.status").value("connected"));
    }
}
