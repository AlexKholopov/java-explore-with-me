package ru.practicum.explore.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> conflictException(final Exception e) {
        log.error(e.getMessage(), e);
        return Map.of("error", e.getMessage());
    }
}
