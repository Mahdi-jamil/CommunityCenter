package com.devesta.blogify.exception.handlers;

import com.devesta.blogify.exception.exceptions.EmailAlreadyExistsException;
import com.devesta.blogify.exception.ErrorMessage;
import com.devesta.blogify.exception.exceptions.UnauthorizedAccessException;
import com.devesta.blogify.exception.exceptions.UserAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
@RestControllerAdvice
public class AuthenticationExceptionHandler {

    Logger logger = LoggerFactory.getLogger(AuthenticationExceptionHandler.class); // todo

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorMessage> handleDuplicateUsernameException(UserAlreadyExistsException ex) {
        ErrorMessage errorDetails = new ErrorMessage(new Date(), ex.getMessage());
        logger.info("{}", errorDetails);
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorMessage> handleDuplicateEmailException(EmailAlreadyExistsException ex) {
        ErrorMessage errorDetails = new ErrorMessage(new Date(), ex.getMessage());
        logger.info("{}", errorDetails);
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorMessage> handleAuthenticationException(AuthenticationException ex) {
        ErrorMessage errorDetails = new ErrorMessage(new Date(), ex.getMessage());
        logger.info("{}", errorDetails);
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ErrorMessage> handleUnauthorizedAccessException(UnauthorizedAccessException ex) {
        ErrorMessage errorDetails = new ErrorMessage(new Date(), ex.getMessage());
        logger.info("{}", errorDetails);
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<ErrorMessage> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        ErrorMessage errorDetails = new ErrorMessage(new Date(), ex.getMessage(), request.getDescription(false));
        logger.info("{}", errorDetails);
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }


}
