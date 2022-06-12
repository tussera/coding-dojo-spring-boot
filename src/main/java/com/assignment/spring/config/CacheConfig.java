package com.assignment.spring.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CaffeineCache weatherCache(
            @Value("${weather.cache.expireAfterWriteInSeconds:30}") Integer expireAfterWriteInSeconds
    ) {
        return new CaffeineCache("weather",
                Caffeine.newBuilder()
                        .expireAfterWrite(expireAfterWriteInSeconds, TimeUnit.SECONDS)
                        .recordStats()
                        .build());
    }

}
