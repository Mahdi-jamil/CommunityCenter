package com.devesta.blogify.exception.handlers;

import com.devesta.blogify.exception.ErrorMessage;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ControllerAdvice
public class ValidationExceptionHandler {

    Logger logger = LoggerFactory.getLogger(ValidationExceptionHandler.class);

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleValidationException(ConstraintViolationException exception) {
        List<String> errors = new ArrayList<>();

        exception.getConstraintViolations().forEach((error) ->
                errors.add(error.getMessage())
        );

        ErrorMessage errorDetails = new ErrorMessage(new Date(), "Not Valid Inputs",errors.toString());
        return new ResponseEntity<>(errorDetails,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<String> errors = new ArrayList<>();

        result.getFieldErrors().forEach((error) ->
                errors.add(error.getField() + ": " + error.getDefaultMessage())
        );
        result.getGlobalErrors().forEach((error) ->
                errors.add(error.getObjectName() + ": " + error.getDefaultMessage())
        );

        if (!errors.isEmpty())
            logger.error("Validation errors: " + errors);

        ErrorMessage errorDetails = new ErrorMessage(new Date(), "Not Valid Inputs",errors.toString());
        return new ResponseEntity<>(errorDetails,HttpStatus.BAD_REQUEST);
    }
}
