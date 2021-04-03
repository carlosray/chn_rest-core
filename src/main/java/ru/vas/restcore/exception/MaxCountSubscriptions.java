package ru.vas.restcore.exception;


import org.springframework.http.HttpStatus;

public class MaxCountSubscriptions extends ApiException {

    public MaxCountSubscriptions() {
        super("Нельзя сохранить больше %d мониторингов", HttpStatus.CONFLICT);
    }

}
