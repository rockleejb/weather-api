package io.github.rockleejb.weatherapi.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import org.pmw.tinylog.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;


@Service
public class CoordinateWeatherService {

    private String owmApiKey;
    private WebClient webClient;
    private ObjectMapper objectMapper;

    public CoordinateWeatherService(String baseUrl, ObjectMapper objectMapper) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
        this.objectMapper = objectMapper;
        Dotenv dotenv = Dotenv.load();
        owmApiKey = dotenv.get("OWM_API_KEY");
    }

    public Map<String, Object> getWeatherByCoordinates(String latitude, String longitude) {
        try {
            double convertedLatitude = Double.parseDouble(latitude);
            double convertedLongitude = Double.parseDouble(longitude);
            Logger.info("Requesting weather by coordinates: latitude {} longitude {}", latitude, longitude);

            JsonNode response = webClient.get()
                    .uri(uriBuilder ->
                            uriBuilder
                                    .queryParam("lat", convertedLatitude)
                                    .queryParam("lon", convertedLongitude)
                                    .queryParam("appid", owmApiKey)
                                    .build())
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
            return transformResponse(response);
        } catch (Exception e) {
            Logger.error("getWeatherByCoordinates failed with exception {}", e);
            throw new RuntimeException("Invalid request");
        }
    }

    public Map<String, Object> transformResponse(JsonNode owmResponse) {
        Map<String, Object> originalResponse = objectMapper.convertValue(owmResponse, new TypeReference<>() {});
        Map<String, Object> transformedResponse = new HashMap<>();
        transformedResponse.put("coordinates", originalResponse.get("coord"));
        transformedResponse.put("weather", originalResponse.get("weather"));
        transformedResponse.put("details", originalResponse.get("main"));
        transformedResponse.put("city", originalResponse.get("name"));
        return transformedResponse;
    }
}
