package ru.vas.restcore.exception.handler;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.vas.restcore.api.dto.ApiError;
import ru.vas.restcore.exception.ApiException;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

@RestControllerAdvice
public class MainExceptionHandler extends BaseExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> resolveException(HttpServletRequest request, Exception exception) throws Exception {
        if (AnnotationUtils.findAnnotation(exception.getClass(), ResponseStatus.class) != null) {
            throw exception;
        }
        return getApiErrorResponse(request, exception, HttpStatus.INTERNAL_SERVER_ERROR, true);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiError> resolveException(HttpServletRequest request, ApiException exception) throws Exception {
        return getApiErrorResponse(request, exception, exception.getHttpStatus(), true);
    }

    // Convert a predefined exception to an HTTP Status code
    @ResponseStatus(value = HttpStatus.CONFLICT,
            reason = "Data integrity violation")  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public void conflict() {
        // Nothing to do
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> resolveMethodNotValid(MethodArgumentNotValidException exception, HttpServletRequest request) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> getExceptionMessage(exception, fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage()))
                .collect(Collectors.joining(";\n"));
        return getApiError(request, exception, HttpStatus.BAD_REQUEST, true)
                .withMessage(message)
                .buildResponse();
    }

}
