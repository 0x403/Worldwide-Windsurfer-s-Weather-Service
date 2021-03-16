package com.example.WorldwideWindsurfer.sWeatherService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidInputException.class)
    public @ResponseBody APIError handleDateTimeParseException(Exception ex) {
        return createAPIError(ex);
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(ThirdPartyAPIException.class)
    public @ResponseBody APIError handleJsonProcessingException(Exception ex) {
        return createAPIError(ex);
    }

    private APIError createAPIError(Exception ex) {
        final String message = ex.getMessage();

        return new APIError(message);
    }

}
