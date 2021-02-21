package ru.vas.restcore.db.domain;


import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import ru.vas.restcore.db.domain.base.BaseTimestampEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true, exclude = {"roleName"})
@Data
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role",
        indexes = @Index(name = "IDX_ROLE_ROLENAME", columnList = "rolename"))
public class RoleEntity extends BaseTimestampEntity implements GrantedAuthority {
    @NotBlank
    @Column(name = "roleName", unique = true, nullable = false)
    private String roleName;

    @Override
    public String getAuthority() {
        return roleName;
    }
}
