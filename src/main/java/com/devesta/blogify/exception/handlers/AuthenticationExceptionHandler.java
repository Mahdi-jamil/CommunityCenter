package com.devesta.blogify.exception.handlers;

import com.devesta.blogify.exception.exceptions.EmailAlreadyExistsException;
import com.devesta.blogify.exception.exceptions.JwtParsingException;
import com.devesta.blogify.exception.exceptions.UnauthorizedAccessException;
import com.devesta.blogify.exception.exceptions.UserAlreadyExistsException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthenticationExceptionHandler {

    Logger logger = LoggerFactory.getLogger(AuthenticationExceptionHandler.class);

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ProblemDetail handleDuplicateUsernameException(UserAlreadyExistsException ex) {
        ProblemDetail problemDetail =
                ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problemDetail.setTitle("User Already Exists");
        problemDetail.setDetail(ex.getMessage());
        logger.error("{}", problemDetail);
        return problemDetail;
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ProblemDetail handleDuplicateEmailException(EmailAlreadyExistsException ex) {
        ProblemDetail problemDetail =
                ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problemDetail.setTitle("Email Already Exists");
        problemDetail.setDetail(ex.getMessage());
        logger.error("{}", problemDetail);
        return problemDetail;
    }

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthenticationException(AuthenticationException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problemDetail.setTitle("Authentication Error");
        problemDetail.setDetail(exception.getMessage());
        logger.error("{}", problemDetail);
        return problemDetail;
    }

    @ExceptionHandler(JwtParsingException.class)
    public ProblemDetail handleJwtError3Exception(JwtParsingException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problemDetail.setTitle("Jwt Error");
        problemDetail.setDetail(exception.getMessage());
        logger.error("{}", problemDetail);
        return problemDetail;
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentialException(BadCredentialsException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problemDetail.setTitle("Authentication Failure");
        problemDetail.setDetail(exception.getMessage());
        logger.error("{}", problemDetail);
        return problemDetail;
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ProblemDetail handleUnauthorizedAccessException(UnauthorizedAccessException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
        problemDetail.setTitle("Access Denied for this Resource");
        problemDetail.setDetail(exception.getMessage());
        logger.error("{}", problemDetail);
        return problemDetail;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDeniedException(AccessDeniedException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
        problemDetail.setTitle("Access Denied for this Resource");
        problemDetail.setDetail(exception.getMessage());
        logger.error("{}", problemDetail);
        return problemDetail;
    }

}
