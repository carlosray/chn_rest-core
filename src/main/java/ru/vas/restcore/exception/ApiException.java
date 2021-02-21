package ru.vas.restcore.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public abstract class ApiException extends RuntimeException{
    @Getter
    private HttpStatus httpStatus;

    public ApiException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public ApiException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }
}
