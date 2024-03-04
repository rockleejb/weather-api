package io.github.rockleejb.weatherapi.model;

import java.util.Map;

public class WeatherResponse {

    Map<String, Object> coord;
    Map<String, Object[]> weather;
    Map<String, String> base;
    Map<String, Object> main;
    Map<String, Integer> visibility;
    Map<String, Object> wind;
    Map<String, Object> rain;
    Map<String, Object> clouds;
    Map<String, Integer> dt;
    Map<String, Object> sys;
    Map<String, Integer> timezone;
    Map<String, Integer> id;
    Map<String, String> name;
    Map<String, Integer> cod;
}
