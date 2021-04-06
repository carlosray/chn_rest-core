package ru.vas.restcore.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.vas.restcore.db.domain.UserEntity;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class UserBaseDTO implements Serializable {
    @Size(max = 255, message = "{validation.Size.email}")
    @Email
    @NotBlank(message = "{validation.NotBlank.email}")
    private String email;

    @Size(max = 255, message = "{validation.Size.firstName}")
    @NotBlank(message = "{validation.NotBlank.firstName}")
    private String firstName;

    @Size(max = 255, message = "{validation.Size.lastName}")
    @NotBlank(message = "{validation.NotBlank.lastName}")
    private String lastName;

    public UserBaseDTO(UserEntity userEntity) {
        this.email = userEntity.getEmail();
        this.firstName = userEntity.getPersonInfo().getFirstName();
        this.lastName = userEntity.getPersonInfo().getLastName();
    }
}
