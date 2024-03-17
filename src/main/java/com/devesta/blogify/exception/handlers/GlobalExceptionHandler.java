package com.devesta.blogify.exception.handlers;

import com.devesta.blogify.exception.ErrorMessage;
import com.devesta.blogify.exception.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorMessage> userNotFoundExceptionHandler(UserNotFoundException exception) {
        ErrorMessage message = new ErrorMessage(new Date(), exception.getMessage());
        logger.info("{}", message);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler(CommunityNotFoundException.class)
    public ResponseEntity<ErrorMessage> communityNotFoundExceptionHandler(CommunityNotFoundException exception) {
        ErrorMessage message = new ErrorMessage(new Date(), exception.getMessage());
        logger.info("{}", message);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ErrorMessage> postNotFoundExceptionHandler(PostNotFoundException exception) {
        ErrorMessage message = new ErrorMessage(new Date(), exception.getMessage());
        logger.info("{}", message);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ErrorMessage> commentNotFoundExceptionHandler(CommentNotFoundException exception) {
        ErrorMessage message = new ErrorMessage(new Date(), exception.getMessage());
        logger.info("{}", message);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler(CommunityNameExistException.class)
    public ResponseEntity<ErrorMessage> communityNameExistsExceptionHandler(CommunityNameExistException exception) {
        ErrorMessage message = new ErrorMessage(new Date(), exception.getMessage());
        logger.info("{}", message);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
    }

    @ExceptionHandler(UserNotJoinedException.class)
    public ResponseEntity<ErrorMessage> userNotJoinedExceptionHandler(UserNotJoinedException exception) {
        ErrorMessage message = new ErrorMessage(new Date(), exception.getMessage());
        logger.info("{}", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(UserAlreadyJoinedException.class)
    public ResponseEntity<ErrorMessage> userAlreadyJoinedExceptionHandler(UserAlreadyJoinedException exception) {
        ErrorMessage message = new ErrorMessage(new Date(), exception.getMessage());
        logger.info("{}", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorMessage> badRequestGlobalHandler(BadRequestException exception) {
        ErrorMessage message = new ErrorMessage(new Date(), "Bad Request",exception.getMessage());
        logger.info("{}", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

}
