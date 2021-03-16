package com.example.WorldwideWindsurfer.sWeatherService;

import com.example.WorldwideWindsurfer.sWeatherService.repository.WindsurfingLocations;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class Application {

	@Bean
	public WebClient getWebClient() {
		return WebClient.create();
	}

	@Bean
	public ObjectMapper mapper() {
		return new ObjectMapper();
	}

	@Bean
	public WindsurfingLocations windsurfingLocations() { return new WindsurfingLocations(); }

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
