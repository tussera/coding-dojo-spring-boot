package com.assignment.spring.mapper;

import com.assignment.spring.api.WeatherExternalApiResponse;
import com.assignment.spring.entity.WeatherEntity;
import com.assignment.spring.model.WeatherResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface WeatherMapper {
    @Mappings({
            @Mapping(target = "city", source = "name"),
            @Mapping(target = "country", source = "sys.country"),
            @Mapping(target = "temperature", source = "main.temp")
    })
    WeatherEntity externalResponseToEntity(WeatherExternalApiResponse externalApiResponse);

    WeatherResponse entityToResponse(WeatherEntity entity);
}
