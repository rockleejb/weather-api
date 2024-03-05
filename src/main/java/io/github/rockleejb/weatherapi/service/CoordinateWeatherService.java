package io.github.rockleejb.weatherapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import org.apache.coyote.BadRequestException;
import org.pmw.tinylog.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;


@Service
public class CoordinateWeatherService {

    private String owmApiKey;
    private WebClient webClient;
    private ObjectMapper objectMapper;

    public CoordinateWeatherService(WebClient webClient, ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
        Dotenv dotenv = Dotenv.load();
        owmApiKey = dotenv.get("OWM_API_KEY");
    }

    public Map<String, Object> getWeatherByCoordinates(String latitude, String longitude) throws BadRequestException {
        try {
            double convertedLatitude = Double.parseDouble(latitude);
            double convertedLongitude = Double.parseDouble(longitude);
            URI uri = UriComponentsBuilder.newInstance().scheme("https")
                    .host("api.openweathermap.org")
                    .path("data").path("/2.5").path("/weather")
                    .queryParam("lat", latitude)
                    .queryParam("lon", longitude)
                    .queryParam("appid", owmApiKey)
                    .build().toUri();
            Logger.info("Requesting weather at uri {} by coordinates: latitude {} longitude {}", uri, latitude, longitude);
            String response = webClient.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            JsonNode jsonNode = objectMapper.readTree(response);
            return objectMapper.convertValue(jsonNode, new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException("Bad query parameters");
        }
    }
}
