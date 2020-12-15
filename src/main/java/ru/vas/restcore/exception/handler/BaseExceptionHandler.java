package ru.vas.restcore.exception.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.vas.restcore.api.dto.ApiError;

import javax.servlet.http.HttpServletRequest;

public abstract class BaseExceptionHandler {
    @Autowired
    private MessageSource messageSource;

    private String getPath(HttpServletRequest request) {
        return request.getRequestURI();
    }

    protected ResponseEntity<ApiError> getApiErrorResponse(HttpServletRequest request, Throwable throwable, HttpStatus status, boolean includeStackTrace) {
        return getApiError(request, throwable, status, includeStackTrace)
                .buildResponse();
    }

    protected ApiError getApiError(HttpServletRequest request, Throwable throwable, HttpStatus status, boolean includeStackTrace) {
        return new ApiError()
                .withStatus(status)
                .withThrowable(throwable)
                .withPath(getPath(request))
                .withIncludeStackTrace(includeStackTrace);
    }

    protected String getExceptionMessage(Throwable throwable, Object ... args) {
        return messageSource.getMessage(String.format("exception.%s", throwable.getClass().getTypeName()), args, LocaleContextHolder.getLocale());
    }

}
