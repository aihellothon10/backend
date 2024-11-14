package com.example.commonmodule.common.exception;

import org.springframework.http.HttpStatus;

public class DataNotFoundException extends BaseException {
    public DataNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
