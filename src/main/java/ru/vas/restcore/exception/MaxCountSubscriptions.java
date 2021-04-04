package ru.vas.restcore.exception;


import org.springframework.http.HttpStatus;

public class MaxCountSubscriptions extends ApiException {

    public MaxCountSubscriptions(long max) {
        super(String.format("Нельзя сохранить больше %d мониторингов", max), HttpStatus.CONFLICT);
    }

}
