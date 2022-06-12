package com.assignment.spring.controller;

import com.assignment.spring.entity.WeatherEntity;
import com.assignment.spring.mapper.WeatherMapper;
import com.assignment.spring.model.WeatherResponse;
import com.assignment.spring.service.WeatherService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/v1/weather")
public class WeatherController {

    private WeatherService weatherService;
    private WeatherMapper weatherMapper;

    public WeatherController(WeatherService weatherService, WeatherMapper weatherMapper) {
        this.weatherService = weatherService;
        this.weatherMapper = weatherMapper;
    }

    @RequestMapping("/{city}")
    public WeatherResponse weather(@NotNull @PathVariable String city) {
        WeatherEntity weatherEntity = weatherService.getWeatherByCity(city);
        return weatherMapper.entityToResponse(weatherEntity);
    }
}
