package ru.vas.restcore.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserRegisterDTO extends UserInfoDTO implements Serializable {
    @NotBlank(message = "{validation.NotBlank.username}")
    @Pattern(regexp = "^[a-zA-Z0-9]{8,32}$", message = "{validation.Pattern.username}")
    private String username;

    @NotBlank(message = "{validation.NotBlank.password}")
    @Pattern(regexp = "^[\\w=!#$%&?-]{8,32}$", message = "{validation.Pattern.password}")
    private String password;

}
