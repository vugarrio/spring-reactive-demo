package com.example.synchronous.geotracking.api.exception;

import com.example.synchronous.geotracking.dto.ErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalErrorWebExceptionHandler extends ResponseEntityExceptionHandler {

    private static final MediaType DEFAULT_MEDIA_TYPE = MediaType.APPLICATION_JSON;
    private static final String ERROR_VALIDATION_FAILED_TEXT = "Validation failed. ";


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> exceptionHandler(Exception ex, ServerWebExchange serverWebExchange) {
        LOGGER.debug("exceptionHandler, message: {}, type: {}", ex.getMessage(), ex.toString());

        ErrorDTO error = new ErrorDTO();
        error.setStatus(INTERNAL_SERVER_ERROR.value());
        error.setError(INTERNAL_SERVER_ERROR.getReasonPhrase());
        error.setMessage(ex.getMessage());
        error.setTimestamp(OffsetDateTime.now());
        error.setPath(serverWebExchange.getRequest().getPath().toString());

        return ResponseEntity.status(INTERNAL_SERVER_ERROR).contentType(DEFAULT_MEDIA_TYPE).body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDTO> handleAllUncaughtException(RuntimeException ex, WebRequest request){

        ErrorDTO error = new ErrorDTO();
        error.setStatus(INTERNAL_SERVER_ERROR.value());
        error.setError(INTERNAL_SERVER_ERROR.getReasonPhrase());
        error.setMessage(ex.getMessage());
        error.setTimestamp(OffsetDateTime.now());
        error.setPath(getRequestURI(request));

        return ResponseEntity.status(INTERNAL_SERVER_ERROR).contentType(DEFAULT_MEDIA_TYPE).body(error);
    }


    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorDTO> exceptionHandler(ResponseStatusException ex, ServerWebExchange serverWebExchange) {
        LOGGER.debug("WebExceptionHandler, message: {}, type: {}", ex.getMessage(), ex.toString());
        ErrorDTO error = new ErrorDTO();
        error.setStatus(ex.getRawStatusCode());
        error.setError(ex.getReason());
        error.setMessage(ex.getMessage());
        error.setTimestamp(OffsetDateTime.now());
        error.setPath(serverWebExchange.getRequest().getPath().toString());

        return ResponseEntity.status(ex.getStatus()).contentType(DEFAULT_MEDIA_TYPE).body(error);
    }



    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        final List<String> errors = new ArrayList<>();

        if (!ex.getBindingResult().getFieldErrors().isEmpty()) {
            for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
                errors.add(String.format("%s: %s", error.getField(), error.getDefaultMessage()));
            }
        }
        if (!ex.getBindingResult().getGlobalErrors().isEmpty()) {
            for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
                errors.add(String.format("%s: %s", error.getObjectName(), error.getDefaultMessage()));
            }
        }

        ErrorDTO error = new ErrorDTO();
        error.setStatus(BAD_REQUEST.value());
        error.setError(BAD_REQUEST.getReasonPhrase());
        error.setMessage(ERROR_VALIDATION_FAILED_TEXT + String.join(", ", errors));
        error.setTimestamp(OffsetDateTime.now());
        error.setPath(getRequestURI(request));

        return ResponseEntity.status(BAD_REQUEST).contentType(DEFAULT_MEDIA_TYPE).body(error);
    }


    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<ErrorDTO> handleConstraintViolation(final ConstraintViolationException ex, final WebRequest request) {
        List<String> errors = new ArrayList<String>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getPropertyPath() + ": " + violation.getMessage());
        }

        ErrorDTO error = new ErrorDTO();
        error.setStatus(BAD_REQUEST.value());
        error.setError(BAD_REQUEST.getReasonPhrase());
        error.setMessage(ERROR_VALIDATION_FAILED_TEXT + String.join(", ", errors));
        error.setTimestamp(OffsetDateTime.now());
        error.setPath(getRequestURI(request));

        return ResponseEntity.status(BAD_REQUEST).contentType(DEFAULT_MEDIA_TYPE).body(error);
    }

    private String getRequestURI(WebRequest request) {
        return ((ServletWebRequest)request).getRequest().getRequestURI();
    }

}
