package ru.vas.restcore.exception;

import org.springframework.http.HttpStatus;

public class UsernameAlreadyExists extends ApiException{
    private static final String message = "Пользователь с таким логином уже существует";

    public UsernameAlreadyExists() {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public UsernameAlreadyExists(Throwable cause) {
        super(message, cause, HttpStatus.BAD_REQUEST);
    }
}
