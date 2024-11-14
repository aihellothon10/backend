package com.example.commonmodule.common.exception;

import org.springframework.http.HttpStatus;

public class LoginFailException extends BaseException {
    public LoginFailException() {
        super(HttpStatus.BAD_REQUEST, "Login fail");
    }
}
