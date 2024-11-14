package com.example.commonmodule.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends RuntimeException {

    private final HttpStatus httpStatus;

    public BaseException(Integer statusCode, String message) {
        super(message);
        this.httpStatus = HttpStatus.valueOf(statusCode);
    }

    public BaseException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }
    
}
