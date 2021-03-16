package com.example.WorldwideWindsurfer.sWeatherService.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

public interface WeatherController {

    @GetMapping("/city/{date}")
    Mono<String> getBestCityForWindsurfing(@PathVariable String date) throws JsonProcessingException;

}
