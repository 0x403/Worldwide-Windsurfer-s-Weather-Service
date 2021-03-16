package com.example.WorldwideWindsurfer.sWeatherService.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import reactor.core.publisher.Mono;

public interface WeatherService {

    Mono<String> getBestCityForWindsurfing(String date) throws JsonProcessingException;

}
