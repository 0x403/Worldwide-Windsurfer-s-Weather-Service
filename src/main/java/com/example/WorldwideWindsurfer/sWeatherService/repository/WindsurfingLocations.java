package com.example.WorldwideWindsurfer.sWeatherService.repository;

import com.example.WorldwideWindsurfer.sWeatherService.model.City;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class WindsurfingLocations {

    private Map<String, City> cities;

    public WindsurfingLocations() {

        cities = new HashMap<>();
        cities.put("Jastarnia", new City("Jastarnia", 54.697172, 18.668233));
        cities.put("Bridgetown", new City("Bridgetown", 13.105655, -59.617621));
        cities.put("Fortaleza", new City("Fortaleza", -3.721850, -38.522499));
        cities.put("Pissouri", new City("Pissouri", 34.667214, 32.710888));
        cities.put("Le Morne", new City("Le Morne", -20.448383, 57.328800));

    }

    public List<String> getCitiesName() {

        List<String> citiesName = new ArrayList<>();

        for(String key : cities.keySet()) {
            citiesName.add(key);
        }

        return citiesName;

    }

}