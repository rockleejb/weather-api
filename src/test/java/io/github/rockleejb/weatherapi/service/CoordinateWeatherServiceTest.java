package io.github.rockleejb.weatherapi.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
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
    private final ObjectMapper objectMapper = new ObjectMapper();
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
    void getWeatherByCoordinates() throws IOException, InterruptedException {
//        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("WeatherApiResponse.json");
//        JsonNode jsonNode = objectMapper.readValue(in, JsonNode.class);
//        mockBackEnd.enqueue(new MockResponse()
//                .setBody(objectMapper.writeValueAsString(jsonNode))
//                .addHeader("Content-Type", "application/json"));
//        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
//        Map<String, Object> response = coordinateWeatherService.getWeatherByCoordinates("41.8781", "-87.6298");
//        assertAll(
//                () -> assertEquals("GET", recordedRequest.getMethod()),
//                () -> assertNotNull(response.get("coordinates")),
//                () -> assertNotNull(response.get("weather")),
//                () -> assertNotNull(response.get("main")),
//                () -> assertEquals("Chicago", response.get("city")),
//                () -> assertNull(response.get("id"))
//        );
    }

    @Test
    void transformResponse() throws IOException {
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