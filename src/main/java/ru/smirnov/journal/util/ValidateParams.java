package ru.smirnov.journal.util;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import java.util.ArrayList;
import java.util.List;

public final class ValidateParams {
    public static final Validator validator = Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory()
            .getValidator();

    private ValidateParams() {
    }

    public static <T> List<String> handleErrorParams(T checkedObj, List<String> params) {
        List<String> errors = new ArrayList<>();
        params.forEach(p -> validator.validateProperty(checkedObj, p)
                .forEach(element -> errors.add(element.getMessage())));
        return errors;
    }
}
