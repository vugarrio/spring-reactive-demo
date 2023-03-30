package com.example.reactive.annotation.geotracking.api.exception;

import com.example.reactive.annotation.geotracking.dto.ErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebfluxRestControllerAdvice {

    private static final MediaType DEFAULT_MEDIA_TYPE = MediaType.APPLICATION_JSON;
    private static final String ERROR_VALIDATION_FAILED_TEXT = "Validation failed.";

    @ExceptionHandler(Exception.class)
    public Mono<ErrorDTO> exceptionHandler(Exception ex, ServerWebExchange serverWebExchange) {
        LOGGER.debug("WebExceptionHandler, message: {}, type: {}", ex.getMessage(), ex.toString());

        ErrorDTO error = new ErrorDTO();
        error.setStatus(INTERNAL_SERVER_ERROR.value());
        error.setError(INTERNAL_SERVER_ERROR.getReasonPhrase());
        error.setMessage(ex.getMessage());

        return generateResponse(error,INTERNAL_SERVER_ERROR, DEFAULT_MEDIA_TYPE, serverWebExchange);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public Mono<ErrorDTO> exceptionHandler(ResponseStatusException ex, ServerWebExchange serverWebExchange) {
        LOGGER.debug("WebExceptionHandler, message: {}, type: {}", ex.getMessage(), ex.toString());
        ErrorDTO error = new ErrorDTO();
        error.setStatus(ex.getRawStatusCode());
        error.setError(ex.getReason());
        error.setMessage(ex.getMessage());

        return generateResponse(error,ex.getStatus(), DEFAULT_MEDIA_TYPE, serverWebExchange);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ErrorDTO> webExchangeBindExceptionHandler(WebExchangeBindException ex, ServerWebExchange serverWebExchange) {
        LOGGER.debug("WebExceptionHandler, message: {}", ex.getMessage());

        final List<String> errors = new ArrayList<>();

        if (!ex.getBindingResult().getFieldErrors().isEmpty()) {
            for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
                errors.add(error.getField() + ": " + error.getDefaultMessage());
            }
        }
        if (!ex.getBindingResult().getGlobalErrors().isEmpty()) {
            for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
                errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
            }
        }

        ErrorDTO error = new ErrorDTO();
        error.setStatus(BAD_REQUEST.value());
        error.setError(BAD_REQUEST.getReasonPhrase());
        error.setMessage(ERROR_VALIDATION_FAILED_TEXT + String.join(", ", errors));

        return generateResponse(error, BAD_REQUEST, DEFAULT_MEDIA_TYPE, serverWebExchange);
    }

    @ExceptionHandler({ ConstraintViolationException.class })
    public Mono<ErrorDTO> handleConstraintViolation(ConstraintViolationException ex, ServerWebExchange serverWebExchange) {
        LOGGER.debug("handleConstraintViolation, message: {}", ex.getMessage());
        List<String> errors = new ArrayList<String>();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getPropertyPath() + ": " + violation.getMessage());
        }

        ErrorDTO error = new ErrorDTO();
        error.setStatus(BAD_REQUEST.value());
        error.setError(BAD_REQUEST.getReasonPhrase());
        error.setMessage(ERROR_VALIDATION_FAILED_TEXT + String.join(", ", errors));

        return generateResponse(error, BAD_REQUEST, DEFAULT_MEDIA_TYPE, serverWebExchange);
    }

    private Mono<ErrorDTO> generateResponse(ErrorDTO error, HttpStatus httpStatus, MediaType mediaType,
                                   ServerWebExchange serverWebExchange) {
        error.setTimestamp(OffsetDateTime.now());
        error.setPath(serverWebExchange.getRequest().getPath().toString());
        serverWebExchange.getResponse().getHeaders().setContentType(mediaType);
        return Mono.just(error);
    }


}
