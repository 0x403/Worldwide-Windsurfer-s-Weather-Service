package com.example.WorldwideWindsurfer.sWeatherService.service;

import com.example.WorldwideWindsurfer.sWeatherService.exceptions.InvalidInputException;
import com.example.WorldwideWindsurfer.sWeatherService.exceptions.ThirdPartyAPIException;
import com.example.WorldwideWindsurfer.sWeatherService.model.ResponseCity;
import com.example.WorldwideWindsurfer.sWeatherService.repository.WeatherRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class WeatherServiceImpl implements WeatherService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private WeatherRepository weatherRepository;

    @Value("${weather.api.key}")
    private String apiKey;

    @Override
    public Mono<String> getBestCityForWindsurfing(String date) {

        LocalDate givenDate = parseStringToLocalDate(date);

        LocalDate today = LocalDate.now();
        int daysDiff = (int) ChronoUnit.DAYS.between(today, givenDate);
        /*
        * check days period
        * */
        if(daysDiff < 0) {
            throw new InvalidInputException("Given date is from the past.");
        } else if (daysDiff > 15) {
            throw new InvalidInputException("We don't have data for more than 16 days.");
        }

        return Flux.fromStream(weatherRepository.getWindsurfingLocationsNames().stream()) // get Stream of the city names
                .flatMap(cityName -> {
                    Mono<JsonNode> cityDataForSpecificDay = getCityDataForSpecificDay(date, cityName);
                    return getResponseCityFromData(cityDataForSpecificDay, cityName);
                })
                .collectList() // Mono<List<ResponseCity>>
                .map(this::getFilteredMap) // Map<Boolean, List<ResponseCity>> get filteredMap by required criteria
                .map(this::getResponse); // String; get response in String from filteredMap
    }

    private LocalDate parseStringToLocalDate(String date) {
        LocalDate localDate;
        try {
            localDate = LocalDate.parse(date);
        } catch (DateTimeParseException ex) {
            throw new InvalidInputException("Wrong date format: " + date + " Your format should be: yyyy-mm-dd");
        }
        return localDate;
    }

    private Mono<JsonNode> getCityDataForSpecificDay(String date, String cityName) {

        return webClient.get()
                .uri("https://api.weatherbit.io/v2.0/forecast/daily?city=" + cityName + "&key=" + apiKey)
                .retrieve()
                .bodyToMono(String.class)
                .map(bodyResponse -> {
                    JsonNode JSONDataField;
                    try {
                        JSONDataField = mapper.readTree(bodyResponse) // convert String to JsonNode
                                .get("data"); // get data field from JSON
                    } catch (JsonProcessingException e) {
                        throw new ThirdPartyAPIException("We got a problem with our API system delivery. We work on it.");
                    }

                    Optional<JsonNode> requestedDayParameters = StreamSupport.stream(JSONDataField.spliterator(), false) // split JSON by day
                            .filter(element -> {
                                return element.get("valid_date").asText().equals(date); // left only requested day
                            })
                            .findFirst();

                    if(requestedDayParameters.isPresent()) {
                        return requestedDayParameters.get();
                    } else {
                        throw new ThirdPartyAPIException("We got a problem with our API system delivery. We work on it.");
                    }
                });

    }

    private Mono<ResponseCity> getResponseCityFromData(Mono<JsonNode> data, String cityName) {
        return data.map(JSONData ->
                new ResponseCity(cityName, JSONData.get("wind_spd").decimalValue(), JSONData.get("temp").decimalValue())
        );
    }

    private Map<Boolean, List<ResponseCity>> getFilteredMap(List<ResponseCity> list) {
        Map<Boolean, List<ResponseCity>> filteredMap = new HashMap<>();
        filteredMap.put(true, new ArrayList<>()); // cities with met condition
        filteredMap.put(false, new ArrayList<>()); // without

        for (ResponseCity responseCity : list) {
            /*
             * if temp = <5, 35> AND wind_speed = <5, 18>
             * */
            if ((responseCity.getTemp().compareTo(new BigDecimal(5)) >= 0
                    ||
                    responseCity.getTemp().compareTo(new BigDecimal(35)) <= 0)
                    &&
                    (responseCity.getWind_spd().compareTo(new BigDecimal(5)) >= 0
                            ||
                            responseCity.getWind_spd().compareTo(new BigDecimal(18)) <= 0)) {

                filteredMap.get(true).add(responseCity);
            } else {
                filteredMap.get(false).add(responseCity);
            }
        }

        return filteredMap;
    }

    private String getResponse(Map<Boolean, List<ResponseCity>> map) {
        if (!map.get(true).isEmpty()) {

            StringBuilder response = new StringBuilder();

            for(ResponseCity responseCity : map.get(true)) {
                response.append(responseCity.getName()).append(" is great place for windsurfing. It's ")
                        .append(responseCity.getTemp()).append(" degree Celsius and ")
                        .append(responseCity.getWind_spd()).append(" m/s wind speed.\n");
            }
            return response.toString();

        } else {

            Optional<ResponseCity> bestPlaceForWindsurfing = map.get(false).stream().max((a, b) -> {
                BigDecimal aResult = a.getWind_spd().multiply(new BigDecimal(3)).add(a.getTemp());
                BigDecimal bResult = b.getWind_spd().multiply(new BigDecimal(3)).add(b.getTemp());
                return aResult.compareTo(bResult);
            });

            if(bestPlaceForWindsurfing.isPresent()) {
                ResponseCity bestPlace = bestPlaceForWindsurfing.get();
                return bestPlace.getName() + " is best place for windsurfing which we can recommend. "
                        + "It's " + bestPlace.getTemp() + " degree Celsius and "
                        + bestPlace.getWind_spd() + " m/s wind speed.";
            } else {
                return "There's no place that we can recommend.";
            }

        }
    }

}