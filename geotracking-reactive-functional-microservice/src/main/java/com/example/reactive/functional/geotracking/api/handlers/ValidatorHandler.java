package com.example.reactive.functional.geotracking.api.handlers;

import com.example.reactive.functional.geotracking.common.util.ErrorsFormater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.server.ServerWebInputException;


@Component
@RequiredArgsConstructor
public class ValidatorHandler {

    private final Validator validator;

    public void validate(Object pojo) {
        String name = pojo.getClass().getName();
        Errors errors = new BeanPropertyBindingResult(pojo, pojo.getClass().getName());
        validator.validate(pojo, errors);

        if (errors.hasErrors()) {
            throw new ServerWebInputException(String.join(", ", ErrorsFormater.formatError(errors)));
        }
    }
}
