package ru.vas.restcore.api.dto;

import lombok.*;
import ru.vas.restcore.db.domain.UserEntity;

import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO extends UserBaseDTO {
    @Size(max = 255, message = "{validation.Size.company}")
    private String company;

    @Size(max = 255, message = "{validation.Size.country}")
    private String country;

    @Size(max = 255, message = "{validation.Size.city}")
    private String city;

    private String info;

    public UserInfoDTO(UserEntity userEntity) {
        super(userEntity);
        this.city = userEntity.getPersonInfo().getCity();
        this.country = userEntity.getPersonInfo().getCountry();
        this.company = userEntity.getPersonInfo().getCompany();
        this.info = userEntity.getPersonInfo().getInfo();
    }
}
