package com.assignment.spring.repository;

import com.assignment.spring.entity.WeatherEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WeatherRepository extends CrudRepository<WeatherEntity, Integer> {
    List<WeatherEntity> findByCity(String city);
}
