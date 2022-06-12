package com.assignment.spring.service;

import com.assignment.spring.api.WeatherExternalApiResponse;
import com.assignment.spring.config.WeatherApiConfig;
import com.assignment.spring.entity.WeatherEntity;
import com.assignment.spring.mapper.WeatherMapper;
import com.assignment.spring.repository.WeatherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = {"weather"})
public class WeatherService {
    private static final Logger LOG = LoggerFactory.getLogger(WeatherService.class);

    private RestTemplate restTemplate;
    private WeatherRepository weatherRepository;
    private WeatherMapper weatherMapper;
    private WeatherApiConfig weatherApiConfig;

    public WeatherService(
            RestTemplate restTemplate,
            WeatherRepository weatherRepository,
            WeatherMapper weatherMapper,
            WeatherApiConfig weatherApiConfig
    ) {
        this.restTemplate = restTemplate;
        this.weatherRepository = weatherRepository;
        this.weatherMapper = weatherMapper;
        this.weatherApiConfig = weatherApiConfig;
    }

    @Cacheable(value = "weather", key = "#city")
    public WeatherEntity getWeatherByCity(@NotNull String city) {
        LOG.info("Fetching weather data for city=" + city);
        String url = weatherApiConfig.url()
                .replace("{city}", city)
                .replace("{appid}", weatherApiConfig.token());
        ResponseEntity<WeatherExternalApiResponse> response = restTemplate.getForEntity(url, WeatherExternalApiResponse.class);
        return saveWeather(response.getBody());
    }

    public WeatherEntity saveWeather(WeatherExternalApiResponse response) {
        Optional<WeatherEntity> weatherEntity = weatherRepository
                .findByCity(response.getName())
                .stream()
                .findFirst();

        if (weatherEntity.isPresent()) {
            WeatherEntity entity = weatherEntity.get();
            entity.setTemperature(response.getMain().getTemp());
            LOG.debug("Weather info for city=" + entity.getCity() + " already present in DB");
            LOG.debug("Updating its current temperature into DB");
            return weatherRepository.save(entity);
        }

        WeatherEntity entity = weatherMapper.externalResponseToEntity(response);
        LOG.debug("Weather info for city=" + entity.getCity() + " not known");
        LOG.debug("Saving its info into DB");
        return weatherRepository.save(entity);
    }
}
