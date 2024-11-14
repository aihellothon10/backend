package com.example.commonmodule.common.exception;

import org.springframework.http.HttpStatus;

public class NoPermissionException extends BaseException {
    public NoPermissionException() {
        super(HttpStatus.UNAUTHORIZED, "No permission");
    }
}
