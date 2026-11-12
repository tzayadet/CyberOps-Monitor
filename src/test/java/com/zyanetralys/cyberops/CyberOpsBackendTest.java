package com.zyanetralys.cyberops;

import org.junit.jupiter.api.Test;
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
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@SpringBootTest
@AutoConfigureMockMvc
class CyberOpsBackendTest {

    @Autowired
    private MockMvc mockMvc;

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

        String firstId = com.jayway.jsonpath.JsonPath.read(first, "$.sessionId");
        String secondId = com.jayway.jsonpath.JsonPath.read(second, "$.sessionId");

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
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist("Access-Control-Allow-Origin"));
    }

    @Test
    void concurrentConnectRequestsNeverExceedSessionCapacity() throws Exception {
        CyberOpsBackend backend = new CyberOpsBackend();
        int requests = 550;
        ExecutorService pool = Executors.newFixedThreadPool(32);
        CountDownLatch start = new CountDownLatch(1);
        AtomicInteger accepted = new AtomicInteger();
        AtomicInteger rejected = new AtomicInteger();

        try {
            java.util.List<Future<?>> futures = new java.util.ArrayList<>();
            for (int i = 0; i < requests; i++) {
                futures.add(pool.submit(() -> {
                    try {
                        start.await();
                        backend.connect();
                        accepted.incrementAndGet();
                    } catch (ResponseStatusException exception) {
                        if (exception.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
                            rejected.incrementAndGet();
                        } else {
                            throw exception;
                        }
                    } catch (InterruptedException exception) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException(exception);
                    }
                }));
            }
            start.countDown();
            for (Future<?> future : futures) {
                future.get();
            }
        } finally {
            pool.shutdownNow();
            backend.shutdownScheduler();
        }

        org.junit.jupiter.api.Assertions.assertEquals(500, accepted.get());
        org.junit.jupiter.api.Assertions.assertEquals(50, rejected.get());
    }

    @Test
    void streamRejectsMalformedSessionIdentifiers() throws Exception {
        mockMvc.perform(get("/api/stream/not_valid!"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void streamRejectsUnknownSessionIdentifiers() throws Exception {
        mockMvc.perform(get("/api/stream/00000000-0000-0000-0000-000000000000"))
                .andExpect(status().isNotFound());
    }

}
