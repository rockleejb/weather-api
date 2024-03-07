package io.github.rockleejb.weatherapi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
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
    private final ObjectMapper objectMapper = new ObjectMapper();
    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }
    @BeforeEach
    void initialize() {
        String baseUrl = "https://api.openweathermap.org/data/2.5/weather";
        coordinateWeatherService = new CoordinateWeatherService(baseUrl, objectMapper);
    }
    @Test
    void getWeatherByCoordinates_happyPath() throws IOException {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("WeatherApiResponse.json");
        JsonNode jsonNode = objectMapper.readValue(in, JsonNode.class);
        MockResponse mockResponse = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody(objectMapper.writeValueAsString(jsonNode));
        mockBackEnd.enqueue(mockResponse);
        Map<String, Object> weatherResponse = coordinateWeatherService.getWeatherByCoordinates("41.8781", "-87.6298");
        assertAll(
                () -> assertNotNull(weatherResponse.get("coordinates")),
                () -> assertNotNull(weatherResponse.get("weather")),
                () -> assertNotNull(weatherResponse.get("details")),
                () -> assertEquals("Chicago", weatherResponse.get("city")),
                () -> assertNull(weatherResponse.get("id"))
        );
    }
    @Test
    void getWeatherByCoordinates_failsWithInvalidParameter() {
        assertThrows(RuntimeException.class,
                () -> coordinateWeatherService.getWeatherByCoordinates("abc", "14.3423"));
    }
    @Test
    void transformResponse() throws IOException {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("OwmResponse.json");
        JsonNode jsonNode = objectMapper.readValue(in, JsonNode.class);
        Map<String, Object> transformedResponse = coordinateWeatherService.transformResponse(jsonNode);
        assertAll(
                () -> assertNotNull(transformedResponse.get("coordinates")),
                () -> assertNotNull(transformedResponse.get("weather")),
                () -> assertNotNull(transformedResponse.get("details")),
                () -> assertEquals("Chicago", transformedResponse.get("city")),
                () -> assertNull(transformedResponse.get("id"))
        );
    }
    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }
}