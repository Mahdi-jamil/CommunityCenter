package com.devesta.blogify.exception.handlers;

import com.devesta.blogify.exception.exceptions.*;
import com.devesta.blogify.exception.exceptions.notfound.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleUserNotFoundException(NotFoundException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle("Not Found");
        problemDetail.setDetail(exception.getMessage());
        logger.error("{}", problemDetail);
        return problemDetail;
    }

    @ExceptionHandler(TokenNotFoundException.class)
    public ProblemDetail tokenNotFoundExceptionHandler(TokenNotFoundException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problemDetail.setTitle("Authentication Failure");
        problemDetail.setDetail(exception.getMessage());
        logger.error("{}", problemDetail);
        return problemDetail;
    }

    @ExceptionHandler(CommunityNameExistException.class)
    public ProblemDetail communityNameExistsExceptionHandler(CommunityNameExistException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problemDetail.setTitle("Duplicate name not allowed");
        problemDetail.setDetail(exception.getMessage());
        logger.error("{}", problemDetail);
        return problemDetail;
    }

    @ExceptionHandler(UserAlreadyJoinedException.class)
    public ProblemDetail userAlreadyJoinedExceptionHandler(UserAlreadyJoinedException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Duplicate name not allowed");
        problemDetail.setDetail(exception.getMessage());
        logger.error("{}", problemDetail);
        return problemDetail;
    }

    @ExceptionHandler(UserNotJoinedException.class)
    public ProblemDetail userNotJoinedExceptionHandler(UserNotJoinedException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setDetail(exception.getMessage());
        logger.error("{}", problemDetail);
        return problemDetail;
    }

    @ExceptionHandler(BadRequestException.class)
    public ProblemDetail userNotJoinedExceptionHandler(BadRequestException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Bad Request");
        problemDetail.setDetail(exception.getMessage());
        logger.error("{}", problemDetail);
        return problemDetail;
    }

}
