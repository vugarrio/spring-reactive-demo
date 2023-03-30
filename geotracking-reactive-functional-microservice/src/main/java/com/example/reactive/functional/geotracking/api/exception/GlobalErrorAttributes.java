package com.example.reactive.functional.geotracking.api.exception;


import com.example.reactive.functional.geotracking.exception.GeoPointServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebInputException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
@Slf4j
public class GlobalErrorAttributes extends DefaultErrorAttributes{

    private static final String KEY_EXCEPTION = "exception";
    private static final String KEY_MESSAGE = "message";
	private static final String KEY_STATUS = "status";
	private static final String KEY_ERROR = "error";
	private static final String ERROR_VALIDATION_FAILED_TEXT = "Validation failed. ";


	@Override
	public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {

		Map<String, Object> map = super.getErrorAttributes(request, options);

		Throwable throwable = getError(request);

        if (throwable instanceof GeoPointServiceException) {
            GeoPointServiceException ex = (GeoPointServiceException) getError(request);
			map.put(KEY_EXCEPTION, ex.getClass().getCanonicalName());
			map.put(KEY_MESSAGE, ex.getMessage());
			LOGGER.error("GeoPointServiceException: {}", map);

        } else if (throwable instanceof ServerWebInputException){
            ServerWebInputException ex = (ServerWebInputException) getError(request);
			map.put(KEY_STATUS, BAD_REQUEST.value());
            map.put(KEY_EXCEPTION, ex.getClass().getCanonicalName());
            map.put(KEY_MESSAGE, Objects.nonNull(ex.getReason()) ? ex.getReason() : ex.getMessage());
			LOGGER.warn("Bad Request, error:{}", map);

		}	else if (throwable instanceof ConstraintViolationException){
			ConstraintViolationException ex = (ConstraintViolationException) getError(request);
			List<String> errors = new ArrayList<String>();
			for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
				errors.add(violation.getPropertyPath() + ": " + violation.getMessage());
			}
			map.put(KEY_STATUS, BAD_REQUEST.value());
			map.put(KEY_ERROR, BAD_REQUEST.getReasonPhrase());
			map.put(KEY_EXCEPTION, ex.getClass().getCanonicalName());
			map.put(KEY_MESSAGE, ERROR_VALIDATION_FAILED_TEXT + String.join(", ", errors));
			LOGGER.warn("Bad Request, error:{}", map);
		}

		else {
			map.put(KEY_EXCEPTION, throwable.getClass().getCanonicalName());
			map.put(KEY_MESSAGE, throwable.getMessage());
			LOGGER.error("unknown server error: {}", map);
		}

		return map;
	}


}
