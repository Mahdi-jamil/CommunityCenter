package com.devesta.blogify.exception.handlers;

import com.devesta.blogify.exception.ErrorMessage;
import com.devesta.blogify.exception.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorMessage> userNotFoundExceptionHandler(UserNotFoundException exception) {
        ErrorMessage message = new ErrorMessage(new Date(), exception.getMessage());
        logger.info("{}", message);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler(CommunityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorMessage> communityNotFoundExceptionHandler(CommunityNotFoundException exception) {
        ErrorMessage message = new ErrorMessage(new Date(), exception.getMessage());
        logger.info("{}", message);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler(PostNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorMessage> postNotFoundExceptionHandler(PostNotFoundException exception) {
        ErrorMessage message = new ErrorMessage(new Date(), exception.getMessage());
        logger.info("{}", message);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler(CommentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorMessage> commentNotFoundExceptionHandler(CommentNotFoundException exception) {
        ErrorMessage message = new ErrorMessage(new Date(), exception.getMessage());
        logger.info("{}", message);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler(CommunityNameExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorMessage> communityNameExistsExceptionHandler(CommunityNameExistException exception) {
        ErrorMessage message = new ErrorMessage(new Date(), exception.getMessage());
        logger.info("{}", message);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
    }

    @ExceptionHandler(UserNotJoinedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorMessage> userNotJoinedExceptionHandler(UserNotJoinedException exception) {
        ErrorMessage message = new ErrorMessage(new Date(), exception.getMessage());
        logger.info("{}", message);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
    }

}
