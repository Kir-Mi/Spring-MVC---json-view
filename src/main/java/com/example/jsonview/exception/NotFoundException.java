package com.example.jsonview.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends BasicException {

    public NotFoundException(String errorMessage, HttpStatus httpStatus) {
        super(errorMessage, httpStatus);
    }
}