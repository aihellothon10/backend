package com.example.commonmodule.common;

import com.example.commonmodule.common.exception.BaseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

@Getter
public class ExceptionResponse {
    private final String timestamp;
    private final HttpStatus status;
    private final String error;
    private final String path;

//    public ExceptionResponse(int status, String error, String path) {
//        this.timestamp = java.time.ZonedDateTime.now(java.time.ZoneOffset.UTC).toString();
//        this.status = status;
//        this.error = error;
//        this.path = path;
//    }

    public ExceptionResponse(HttpStatus status, String error, String path) {
        this.timestamp = java.time.ZonedDateTime.now(java.time.ZoneOffset.UTC).toString();
        this.status = status;
        this.error = error;
        this.path = path;
    }

    public ExceptionResponse(BaseException baseException, WebRequest request) {
        this.timestamp = java.time.ZonedDateTime.now(java.time.ZoneOffset.UTC).toString();
        this.status = baseException.getHttpStatus();
        this.error = baseException.getMessage();
        this.path = request.getDescription(false).substring(4);
    }

    public static ExceptionResponse of(BaseException baseException, WebRequest request) {
        return new ExceptionResponse(baseException, request);
    }

//    public static ExceptionResponse of(int status, String error) {
//        return new ExceptionResponse(status, error, null);
//    }

    public static ExceptionResponse of(HttpStatus status, String error) {
        return new ExceptionResponse(status, error, null);
    }


//    public static ExceptionResponse of(int status, String error, String path) {
//        return new ExceptionResponse(status, error, path);
//    }

    public static ExceptionResponse of(HttpStatus status, String error, String path) {
        return new ExceptionResponse(status, error, path);
    }

    public static ExceptionResponse loginFail() {
        return ExceptionResponse.of(HttpStatus.BAD_REQUEST, "Login Fail", "/api/login");
    }

    public String toJson() throws JsonProcessingException {
        return JsonUtil.toJson(this);
    }

    public ResponseEntity<ExceptionResponse> toResponseEntity() {
        return ResponseEntity.status(status).body(new ExceptionResponse(status, error, path));
    }

}
