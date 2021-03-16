package com.example.WorldwideWindsurfer.sWeatherService.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ResponseCity {

    private final String name;
    private final BigDecimal wind_spd;
    private final BigDecimal temp;

}
