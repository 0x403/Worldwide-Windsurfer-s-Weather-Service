package com.example.WorldwideWindsurfer.sWeatherService.exceptions;

import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class APIError {

    private final String timestamp;
    private final String message;

    public APIError(String message) {
        timestamp = ZonedDateTime.now().toString();
        this.message = message;
    }

}
