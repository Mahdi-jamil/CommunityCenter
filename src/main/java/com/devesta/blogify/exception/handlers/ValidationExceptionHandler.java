package com.devesta.blogify.exception.handlers;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ValidationExceptionHandler {

    Logger logger = LoggerFactory.getLogger(ValidationExceptionHandler.class);

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleValidationException(ConstraintViolationException exception) {
        Map<String, Object> errors = exception.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        ConstraintViolation::getMessage
                ));

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Not Valid Inputs");
        problemDetail.setProperties(errors);
        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        Map<String, Object> errors = new HashMap<>();

        result.getFieldErrors().forEach((error) ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        result.getGlobalErrors().forEach((error) ->
                errors.put(error.getObjectName(), error.getDefaultMessage())
        );

        if (!errors.isEmpty())
            logger.error("Validation errors: " + errors);
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Not Valid Inputs");
        problemDetail.setProperties(errors);

        return problemDetail;
    }
}
