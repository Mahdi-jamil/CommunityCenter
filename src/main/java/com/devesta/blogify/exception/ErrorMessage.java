package com.devesta.blogify.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
public class ErrorMessage {

    private Date date;
    private String message;
    private String details;

    public ErrorMessage(Date date, String message) {
        this.date = date;
        this.message = message;
    }
}
