package com.example.WorldwideWindsurfer.sWeatherService.exceptions;

public class ThirdPartyAPIException extends RuntimeException {

    public ThirdPartyAPIException() {
    }

    public ThirdPartyAPIException(String message) {
        super(message);
    }

    public ThirdPartyAPIException(String message, Throwable cause) {
        super(message, cause);
    }

    public ThirdPartyAPIException(Throwable cause) {
        super(cause);
    }
}
