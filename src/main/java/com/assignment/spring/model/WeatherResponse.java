package com.assignment.spring.model;

public record WeatherResponse(
   String city,
   String country,
   Double temperature
){ }
