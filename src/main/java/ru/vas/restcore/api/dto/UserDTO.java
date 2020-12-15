package ru.vas.restcore.api.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserDTO {
    @NotBlank(message = "{test.validation}")
    private String username;
    @NotBlank(message = "{test.validation}")
    private String password;
}
