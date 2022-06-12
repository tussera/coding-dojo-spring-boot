package com.assignment.spring.service;

import com.assignment.spring.TestData;
import com.assignment.spring.config.WeatherApiConfig;
import com.assignment.spring.entity.WeatherEntity;
import com.assignment.spring.exception.CityWeatherInfoNotFoundException;
import com.assignment.spring.mapper.WeatherMapper;
import com.assignment.spring.repository.WeatherRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = WeatherService.class)
@EnableConfigurationProperties(WeatherApiConfig.class)
@DisplayName("Weather Service Test Case")
class WeatherServiceTest {

    @MockBean
    private RestTemplate restTemplate;
    @MockBean
    private WeatherRepository weatherRepository;
    @MockBean
    private WeatherMapper weatherMapper;
    @Autowired
    private WeatherService weatherService;
    @Autowired
    private WeatherApiConfig weatherApiConfig;

    @Test
    @DisplayName("Should get weather info for a given City")
    void shouldGetWeatherInfoForAGivenCity() {
        // GIVEN
        WeatherEntity expectedCityInfo = TestData.getWeatherEntityExample();
        when(restTemplate.getForEntity(eq("http://test/weather?q=Amsterdam&appid=myKey"), any()))
                .thenReturn(new ResponseEntity<>(TestData.generateWeatherResponseFromTestFile(), HttpStatus.OK));
        when(weatherMapper.externalResponseToEntity(any()))
                .thenReturn(TestData.getWeatherEntityExample());
        when(weatherRepository.save(any())).thenReturn(TestData.getWeatherEntityExample());
        // WHEN
        WeatherEntity result = weatherService.getWeatherByCity(TestData.CITY);
        // THEN
        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expectedCityInfo);
    }

    @Test
    @DisplayName("Should throw CityWeatherInfoNotFoundException when no weather info found for a given city")
    void shouldThrowExceptionWhenNoWeatherInfoForGivenCity() {
        // GIVEN
        when(restTemplate.getForEntity(eq("http://test/weather?q=Amsterdam&appid=myKey"), any()))
                .thenThrow(HttpClientErrorException.NotFound.class);
        // THEN
        assertThatExceptionOfType(CityWeatherInfoNotFoundException.class)
                .isThrownBy(() -> weatherService.getWeatherByCity(TestData.CITY));
    }
}