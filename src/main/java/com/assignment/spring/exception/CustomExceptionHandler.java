package com.assignment.spring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    private ResponseEntity<Object> buildResponseEntity(ApiErrorResponse apiErrorResponse) {
        return new ResponseEntity<>(apiErrorResponse, apiErrorResponse.status());
    }

    @ExceptionHandler(value = CityWeatherInfoNotFoundException.class)
    public ResponseEntity<Object> onApiException(CityWeatherInfoNotFoundException exception) {
        return buildResponseEntity(
                new ApiErrorResponse(
                        LocalDateTime.now(),
                        exception.getMessage(),
                        HttpStatus.NOT_FOUND
                )
        );
    }

    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public ResponseEntity<Object> onUnauthorizedApiRequest(HttpClientErrorException exception) {
        return buildResponseEntity(
                new ApiErrorResponse(
                        LocalDateTime.now(),
                        "Invalid API Key",
                        HttpStatus.UNAUTHORIZED
                )
        );
    }
}
