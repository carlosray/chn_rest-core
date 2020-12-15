package ru.vas.restcore.api.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Optional;

/**
 * Общий класс ответа в случае ошибки
 */
@AllArgsConstructor
@NoArgsConstructor
@With
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"timestamp", "status", "error", "message", "path", "exception", "trace"})
public class ApiError {
    @JsonIgnore
    private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    @JsonIgnore
    private Throwable throwable;
    @JsonIgnore
    private String message;
    @JsonIgnore
    private boolean isIncludeStackTrace = false;
    @JsonIgnore
    private String path;

    @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS")
    public Date getTimestamp() {
        return new Date();
    }

    @JsonProperty("status")
    public Integer getStatus() {
        return status.value();
    }

    @JsonProperty("error")
    public String getError() {
        return status.getReasonPhrase();
    }

    @JsonProperty("message")
    public String getMessage() {
        return Optional.ofNullable(message)
                .orElseGet(() -> Optional.ofNullable(throwable)
                        .map(Throwable::getMessage)
                        .orElse("No message available"));
    }

    @JsonProperty("path")
    public String getPath() {
        return path;
    }

    @JsonProperty("exception")
    public String getException() {
        return Optional.ofNullable(throwable)
                .map(Object::getClass)
                .map(Class::getName)
                .orElse(null);
    }

    @JsonProperty("trace")
    public String getStackTrace() {
        return isIncludeStackTrace ?
                Optional.ofNullable(throwable)
                        .map(thrw -> {
                            StringWriter stackTrace = new StringWriter();
                            thrw.printStackTrace(new PrintWriter(stackTrace));
                            stackTrace.flush();
                            return stackTrace.toString();
                        })
                        .orElse(null) :
                null;
    }

    public ResponseEntity<ApiError> buildResponse() {
        return ResponseEntity
                .status(status)
                .body(this);
    }
}
