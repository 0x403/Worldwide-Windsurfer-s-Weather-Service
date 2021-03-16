package com.example.WorldwideWindsurfer.sWeatherService.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WeatherRepository {

    @Autowired
    private WindsurfingLocations windsurfingLocations;

    public WindsurfingLocations getWindsurfingLocations() {
        return windsurfingLocations;
    }

    public List<String> getWindsurfingLocationsNames() {
        return windsurfingLocations.getCitiesName();
    }

}
