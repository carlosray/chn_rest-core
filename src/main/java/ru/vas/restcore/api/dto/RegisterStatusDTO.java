package ru.vas.restcore.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public enum RegisterStatusDTO {
    SUCCESS("Успешно создан пользователь"),
    FAILURE("Пользователь не создан");

    private final String message;
    @JsonProperty("status")
    private final String status = this.name();

    @JsonValue
    public Map<String, String> toAnswer() {
        final Map<String, String> map = new HashMap<>();
        map.put("message", this.message);
        map.put("status", this.status);
        return map;
    }

}
