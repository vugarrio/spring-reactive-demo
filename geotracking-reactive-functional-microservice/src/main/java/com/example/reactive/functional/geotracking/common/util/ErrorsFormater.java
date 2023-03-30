/**
 * 
 */
package com.example.reactive.functional.geotracking.common.util;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;


public class ErrorsFormater {

	public static List<String> formatError(Errors errors) {
        final List<String> errorsFormat = new ArrayList<>();
        for (FieldError error : errors.getFieldErrors()) {
            errorsFormat.add(String.format("%s: %s", error.getField(), error.getDefaultMessage()));
        }
        return errorsFormat;
    }
}
