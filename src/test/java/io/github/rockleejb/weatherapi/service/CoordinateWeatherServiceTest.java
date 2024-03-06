package io.github.rockleejb.weatherapi.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CoordinateWeatherServiceTest {
    private static MockWebServer mockBackEnd;
    private CoordinateWeatherService coordinateWeatherService;
    private ObjectMapper objectMapper;
    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    @BeforeEach
    void initialize() {
        String baseUrl = String.format("http://localhost:%s",
                mockBackEnd.getPort());
        coordinateWeatherService = new CoordinateWeatherService(baseUrl, objectMapper);
    }
    @Test
    void transformResponse() throws IOException {
        objectMapper = new ObjectMapper();
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("OwmResponse.json");
        JsonNode jsonNode = objectMapper.readValue(in, JsonNode.class);
        Map<String, Object> stringObjectMap = objectMapper.convertValue(jsonNode, new TypeReference<>() {});
        Map<String, Object> transformedResponse = coordinateWeatherService.transformResponse(stringObjectMap);
        assertAll(
                () -> assertNotNull(transformedResponse.get("coordinates")),
                () -> assertNotNull(transformedResponse.get("weather")),
                () -> assertNotNull(transformedResponse.get("main")),
                () -> assertEquals("Chicago", transformedResponse.get("city")),
                () -> assertNull(transformedResponse.get("id"))
        );
    }
    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }
}