package io.github.rockleejb.weatherapi.service;

import org.pmw.tinylog.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import io.github.cdimascio.dotenv.Dotenv;
import reactor.core.publisher.Mono;


@Service
public class CoordinateWeatherService {

    private String owmApiKey;
    private String owmUrl;
    private WebClient webClient;

    public CoordinateWeatherService(WebClient webClient) {
        this.webClient = webClient;
        Dotenv dotenv = Dotenv.load();
        owmApiKey = dotenv.get("OWM_API_KEY");
        owmUrl = dotenv.get("OWM_URL");
    }

    public void getWeatherByCoordinates(double latitude, double longitude) {
        URI uri = UriComponentsBuilder.fromUriString(owmUrl).queryParam("lat", latitude).queryParam("long", longitude)
                .queryParam("appid", owmApiKey).build().toUri();
        Logger.info("Requesting weather at uri {} by coordinates: latitude {} longitude {}", uri.toString(), latitude, longitude);
//        String response = webClient.get()
//                .uri(uri)
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//        System.out.println(response);
    }
}
