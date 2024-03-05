package io.github.rockleejb.weatherapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CoordinateWeatherServiceTest {
    @InjectMocks
    private CoordinateWeatherService coordinateWeatherService;
    @Mock
    private WebClient webClientMock;
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpecMock;
    @Mock
    private WebClient.ResponseSpec responseSpecMock;
    private String latitude = "12";
    private String longitude = "23";
    @Test
    void getWeatherByCoordinates() {
        String response = new HashMap<String, Object>().toString();
        when(webClientMock.get())
                .thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri(any(URI.class)))
                .thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve())
                .thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(String.class))
                .thenReturn(Mono.just(response));
        assertEquals(response,
                coordinateWeatherService.getWeatherByCoordinates(latitude, longitude));
    }
}