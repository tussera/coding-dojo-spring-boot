package com.assignment.spring;

import com.assignment.spring.api.WeatherExternalApiResponse;
import com.assignment.spring.entity.WeatherEntity;
import com.assignment.spring.model.WeatherResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestData {

    public static final String CITY = "Amsterdam";
    public static final String COUNTRY = "NL";
    public static final Double TEMPERATURE = Double.valueOf("290.00");
    public static final String WEATHER_RESPONSE_EXAMPLE_FILE_NAME = "weatherApiResponseExample.json";
    public static WeatherEntity getWeatherEntityExample() {
        WeatherEntity entity = new WeatherEntity();
        entity.setCity(CITY);
        entity.setCountry(COUNTRY);
        entity.setTemperature(TEMPERATURE);
        return  entity;
    }

    public static WeatherResponse getWeatherResponseExample() {
        return new WeatherResponse(CITY, COUNTRY, TEMPERATURE);
    }

    public static WeatherExternalApiResponse generateJokesFromTestFile() {
        WeatherExternalApiResponse weatherExternalApiResponse;
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        ClassLoader classLoader = TestData.class.getClassLoader();

        try {
            URL url = classLoader.getResource(WEATHER_RESPONSE_EXAMPLE_FILE_NAME);
            weatherExternalApiResponse = mapper.readValue(url, WeatherExternalApiResponse.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return weatherExternalApiResponse;
    }

    public static String getResponseAsString() {
        ClassLoader classLoader = TestData.class.getClassLoader();
        try {
            return Files.readString(Paths.get(classLoader.getResource(WEATHER_RESPONSE_EXAMPLE_FILE_NAME).toURI()));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
