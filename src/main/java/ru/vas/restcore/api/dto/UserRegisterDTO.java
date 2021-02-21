package ru.vas.restcore.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Size;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserRegisterDTO extends UserLoginDTO implements Serializable {
    @Size(max = 255, message = "{validation.Size.firstName}")
    private String firstName;

    @Size(max = 255, message = "{validation.Size.lastName}")
    private String lastName;

    @Size(max = 255, message = "{validation.Size.company}")
    private String company;

    @Size(max = 255, message = "{validation.Size.country}")
    private String country;

    @Size(max = 255, message = "{validation.Size.city}")
    private String city;

    private String info;

}
