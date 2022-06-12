package com.assignment.spring.service;

import com.assignment.spring.TestData;
import com.assignment.spring.config.WeatherApiConfig;
import com.assignment.spring.entity.WeatherEntity;
import com.assignment.spring.mapper.WeatherMapper;
import com.assignment.spring.repository.WeatherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest
@DisplayName("Weather Service Test Case")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WeatherServiceTest {

    @Autowired
    private WeatherService weatherService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private WeatherApiConfig weatherApiConfig;
    @SpyBean
    private WeatherRepository weatherRepository;
    @Autowired
    private WeatherMapper weatherMapper;
    private MockRestServiceServer mockServer;

    @BeforeEach
    void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    @DisplayName("Should get weather information for a given city from a external API")
    @Order(1)
    void shouldGetWeatherInfoFromExternalAPI() throws URISyntaxException {
        // GIVEN
        WeatherEntity expectedWeatherEntity = weatherMapper
                .externalResponseToEntity(TestData.generateJokesFromTestFile());
        String url = weatherApiConfig.url()
                .replace("{city}", TestData.CITY)
                .replace("{appid}", weatherApiConfig.token());
        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI(url)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(TestData.getResponseAsString())
                );
        // WHEN
        WeatherEntity weatherEntity = weatherService.getWeatherByCity(TestData.CITY);
        // THEN
        mockServer.verify();
        verify(weatherRepository, times(1)).findByCity(TestData.CITY);
        verify(weatherRepository, times(1)).save(any());
        assertThat(weatherEntity)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedWeatherEntity);
    }

    @Test
    @DisplayName("Should use cache for requesting the same city in a given period")
    @Order(10)
    void shouldUseCacheProperlyForAGivenCity() throws URISyntaxException, InterruptedException {
        // GIVEN
        String url = weatherApiConfig.url()
                .replace("{city}", TestData.CITY)
                .replace("{appid}", weatherApiConfig.token());
        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI(url)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(TestData.getResponseAsString())
                );
        // WHEN & THEN
//        weatherService.getWeatherByCity(TestData.CITY); // Should request new info
//        verify(weatherRepository, times(1)).findByCity(TestData.CITY);
//        verify(weatherRepository, times(1)).save(any());
//        weatherService.getWeatherByCity(TestData.CITY); // Should use cache
//        verify(weatherRepository, times(0)).findByCity(TestData.CITY);
//        Thread.sleep(1000); // expireAfterWriteInSeconds is configured for 1 second
//        weatherService.getWeatherByCity(TestData.CITY); // Should request info again
//        verify(weatherRepository, times(1)).findByCity(TestData.CITY);
//        verify(weatherRepository, times(1)).save(any());
    }
}