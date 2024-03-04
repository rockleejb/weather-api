package io.github.rockleejb.weatherapi.controller;

import io.github.rockleejb.weatherapi.service.CoordinateWeatherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/weather")
public class WeatherController {

    private final CoordinateWeatherService coordinateWeatherService;

    public WeatherController(CoordinateWeatherService coordinateWeatherService) {
        this.coordinateWeatherService = coordinateWeatherService;
    }

    @GetMapping(value = "/{latitude}/{longitude}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> getWeatherByLatitudeAndLongitude(@PathVariable("latitude") String latitude,
                                                                 @PathVariable("longitude") String longitude) {
        try {
            double convertedLatitude = Double.parseDouble(latitude);
            double convertedLongitude = Double.parseDouble(longitude);
            coordinateWeatherService.getWeatherByCoordinates(convertedLatitude, convertedLongitude);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
