package io.github.rockleejb.weatherapi.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import org.pmw.tinylog.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class CoordinateWeatherService {

    private String owmApiKey;
    private ObjectMapper objectMapper;

    public CoordinateWeatherService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        Dotenv dotenv = Dotenv.load();
        owmApiKey = dotenv.get("OWM_API_KEY");
    }

    public Map<String, Object> getWeatherByCoordinates(String latitude, String longitude) throws NumberFormatException {
        double convertedLatitude = Double.parseDouble(latitude);
        double convertedLongitude = Double.parseDouble(longitude);
        Logger.info("Requesting weather by coordinates: latitude {} longitude {}", latitude, longitude);
        WebClient webClient = WebClient.builder().baseUrl("https://api.openweathermap.org/data/2.5/weather").build();
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

    public Map<String, Object> getWeatherByCityName(String city) throws FileNotFoundException {
        List<Map<String, Object>> geolocationResponse = getCoordinatesFromCityName(city);
        return getWeatherByCoordinates(String.valueOf(geolocationResponse.get(0).get("lat")),
                String.valueOf(geolocationResponse.get(0).get("lon")));
    }

    public List<Map<String, Object>> getCoordinatesFromCityName(String city) throws FileNotFoundException {
        Logger.info("Requesting coordinates by city name: {}", city);
        WebClient webClient = WebClient.builder().baseUrl("http://api.openweathermap.org/geo/1.0/direct").build();
        JsonNode response = webClient.get()
                .uri(uriBuilder ->
                        uriBuilder
                                .queryParam("q", city)
                                .queryParam("appid", owmApiKey)
                                .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
        List<Map<String, Object>> geolocationResponse = objectMapper.convertValue(response, new TypeReference<>() {});
        if(geolocationResponse.get(0).get("lat") == null || geolocationResponse.get(0).get("lon") == null) {
            throw new FileNotFoundException("No geolocation response found");
        }
        return geolocationResponse;
    }
}
