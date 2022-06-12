package com.assignment.spring.exception;

public class CityWeatherInfoNotFoundException extends RuntimeException{
    public CityWeatherInfoNotFoundException(String message) {
        super(message);
    }
}
