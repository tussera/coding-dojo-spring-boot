package com.assignment.spring.controller;

import com.assignment.spring.TestData;
import com.assignment.spring.entity.WeatherEntity;
import com.assignment.spring.mapper.WeatherMapper;
import com.assignment.spring.service.WeatherService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WeatherController.class)
@DisplayName("Weather Controller Test Case")
class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private WeatherService weatherService;
    @MockBean
    private WeatherMapper weatherMapper;

    @Test
    @DisplayName("Should successfully return the weather for a given City")
    void shouldSuccessfullyReturnRandomJoke() throws Exception {
        // GIVEN
        WeatherEntity expectedWeatherEntity = TestData.getWeatherEntityExample();
        when(weatherService.getWeatherByCity(any())).thenReturn(expectedWeatherEntity);
        when(weatherMapper.entityToResponse(expectedWeatherEntity)).thenReturn(TestData.getWeatherResponseExample());
        // THEN
        mockMvc.perform(get("/api/weather/" + expectedWeatherEntity.getCity()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value(expectedWeatherEntity.getCity()))
                .andExpect(jsonPath("$.country").value(expectedWeatherEntity.getCountry()))
                .andExpect(jsonPath("$.temperature").value(expectedWeatherEntity.getTemperature()));
    }

    @Test
    @DisplayName("Should return NOT_FOUND when no info found for a given City")
    void shouldReturnNotFoundWhenNoInfoFoundForGivenCity() throws Exception {
        // GIVEN
        when(weatherService.getWeatherByCity(any()))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
        // THEN
        mockMvc.perform(get("/api/weather/" + TestData.CITY))
                .andExpect(status().isNotFound());
    }
}