package io.github.rockleejb.weatherapi.service;

import io.github.cdimascio.dotenv.Dotenv;
import org.pmw.tinylog.Logger;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;


@Service
public class CoordinateWeatherService {

    private String owmApiKey;
    private WebClient webClient;

    public CoordinateWeatherService(WebClient webClient) {
        this.webClient = webClient;
        Dotenv dotenv = Dotenv.load();
        owmApiKey = dotenv.get("OWM_API_KEY");
    }

    public void getWeatherByCoordinates(double latitude, double longitude) {
        URI uri = UriComponentsBuilder.newInstance().scheme("https")
                .host("api.openweathermap.org")
                .path("data").path("/2.5").path("/weather")
                .queryParam("lat", latitude).queryParam("lon", longitude)
                .queryParam("appid", owmApiKey).build().toUri();
        Logger.info("Requesting weather at uri {} by coordinates: latitude {} longitude {}", uri, latitude, longitude);
        String response = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        System.out.println(response);
    }
}
