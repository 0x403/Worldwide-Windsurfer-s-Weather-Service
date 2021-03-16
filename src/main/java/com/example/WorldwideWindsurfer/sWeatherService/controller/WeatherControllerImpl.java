package com.example.WorldwideWindsurfer.sWeatherService.controller;

import com.example.WorldwideWindsurfer.sWeatherService.service.WeatherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class WeatherControllerImpl implements WeatherController {

    @Autowired
    private WeatherService weatherService;

    @Override
    public Mono<String> getBestCityForWindsurfing(String date) throws JsonProcessingException {

        return weatherService.getBestCityForWindsurfing(date);

    }
}
