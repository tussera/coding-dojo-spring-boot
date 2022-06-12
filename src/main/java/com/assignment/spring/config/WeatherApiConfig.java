package com.assignment.spring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "weather.api")
@ConstructorBinding
public record WeatherApiConfig(String url, String token) { }
