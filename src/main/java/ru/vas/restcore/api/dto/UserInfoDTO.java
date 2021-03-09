package ru.vas.restcore.api.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO implements Serializable {
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

    @Size(max = 255, message = "{validation.Size.company}")
    private String company;

    @Size(max = 255, message = "{validation.Size.country}")
    private String country;

    @Size(max = 255, message = "{validation.Size.city}")
    private String city;

    private String info;
}
