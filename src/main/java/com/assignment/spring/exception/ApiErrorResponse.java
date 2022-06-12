package com.assignment.spring.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * Response wrapper for API Errors
 */
public record ApiErrorResponse(
    LocalDateTime timestamp,
    String message,
    HttpStatus status
) { }
