package io.github.rockleejb.weatherapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebClientConfiguration {
    @Bean
    public String baseUrl() {
        return "https://api.openweathermap.org/data/2.5/weather";
    }
}
