package io.github.rockleejb.weatherapi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CoordinateWeatherServiceTest {
    private CoordinateWeatherService coordinateWeatherService;
    private WebClient webClient;
    private ObjectMapper objectMapper = new ObjectMapper();
    private String apiKey;
    private static MockWebServer mockBackEnd;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }
    @BeforeEach
    void init() throws NoSuchFieldException, IllegalAccessException {
        coordinateWeatherService = new CoordinateWeatherService(objectMapper);
        Field field = coordinateWeatherService.getClass().getDeclaredField("owmApiKey");
        field.setAccessible(true);
        apiKey = (String) field.get(coordinateWeatherService);
    }
    @Test
    void getWeatherByCoordinates_happyPath() throws IOException {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=41.8781&lon=-87.623177&appid=" + apiKey;
        mockBackEnd.url(url);
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("WeatherApiResponse.json");
        byte[] jsonBytes = IOUtils.toByteArray(Objects.requireNonNull(in));
        MockResponse mockResponse = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody(new String(jsonBytes));
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
        assertThrows(RuntimeException.class,
                () -> coordinateWeatherService.getWeatherByCoordinates("3.2345", "foo"));
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

    @Test
    void getCoordinatesFromCityName() throws IOException {
        String url = "http://api.openweathermap.org/geo/1.0/direct?q=Chicago&limit=1&appid=" + apiKey;
        mockBackEnd.url(url);
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("GeolocationResponse.json");
        byte[] jsonBytes = IOUtils.toByteArray(Objects.requireNonNull(in));
        MockResponse mockResponse = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody(new String(jsonBytes));
        mockBackEnd.enqueue(mockResponse);
        List<Map<String, Object>> geolocationResponse = coordinateWeatherService.getCoordinatesFromCityName("Chicago");
        assertAll(
                () -> assertFalse(geolocationResponse.isEmpty())
        );
    }
    @Test
    void testGetCoordinatesFromCityName_invalidCityNameThrowsFileNotFoundException() {
        assertThrows(FileNotFoundException.class, () -> coordinateWeatherService.getCoordinatesFromCityName("DarthVader"));
    }
}