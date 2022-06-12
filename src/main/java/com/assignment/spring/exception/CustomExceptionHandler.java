package com.assignment.spring.exception;

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

    @ExceptionHandler(value = HttpClientErrorException.class)
    public ResponseEntity<Object> onApiException(HttpClientErrorException exception) {
        return buildResponseEntity(
                new ApiErrorResponse(
                        LocalDateTime.now(),
                        exception.getMessage(),
                        exception.getStatusCode()
                )
        );
    }
}
