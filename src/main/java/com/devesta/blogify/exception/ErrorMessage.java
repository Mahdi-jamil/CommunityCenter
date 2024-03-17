package com.devesta.blogify.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
public class ErrorMessage {

    @JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date date;
    private String message;
    private String details;

    public ErrorMessage(Date date, String message) {
        this.date = date;
        this.message = message;
    }
}
