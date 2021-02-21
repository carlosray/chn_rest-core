package ru.vas.restcore.api.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
public class UserLoginDTO implements Serializable {
    @NotBlank(message = "{validation.NotBlank}")
    @Pattern(regexp = "^[a-zA-Z0-9]{8,32}$", message = "{validation.Pattern.username}")
    private String username;

    @NotBlank(message = "{validation.NotBlank}")
    @Pattern(regexp = "^[\\w=!#$%&?-]{8,32}$", message = "{validation.Pattern.password}")
    private String password;
}
