package io.github.rockleejb.weatherapi.controller;

import io.github.rockleejb.weatherapi.service.CoordinateWeatherService;
import org.apache.coyote.BadRequestException;
import org.pmw.tinylog.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.FileNotFoundException;
import java.util.Map;

@Controller
@RequestMapping("/weather")
public class WeatherController {

    private final CoordinateWeatherService coordinateWeatherService;

    public WeatherController(CoordinateWeatherService coordinateWeatherService) {
        this.coordinateWeatherService = coordinateWeatherService;
    }

    @GetMapping(value = "/{latitude}/{longitude}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getWeatherByLatitudeAndLongitude(@PathVariable("latitude") String latitude,
                                                                                     @PathVariable("longitude") String longitude) throws BadRequestException {
            Map<String, Object> weatherDescription = coordinateWeatherService.getWeatherByCoordinates(latitude, longitude);
            Logger.info("Weather request by coordinates succeeded");
            return new ResponseEntity<>(weatherDescription, HttpStatus.OK);
    }

    @GetMapping(value =  "/{city}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getWeatherByCity(@PathVariable("city") String city) throws FileNotFoundException, BadRequestException {
        Map<String, Object> weatherDescription = coordinateWeatherService.getWeatherByCityName(city);
        Logger.info("Weather request by city name succeeded");
        return new ResponseEntity<>(weatherDescription, HttpStatus.OK);
    }
}
