package ru.vas.restcore.db.domain;

import lombok.*;
import ru.vas.restcore.db.domain.base.BaseTimestampEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user",
        indexes = @Index(name = "IDX_USER_USERNAME", columnList = "username"))
public class User extends BaseTimestampEntity {
    @NotBlank
    @Column(name = "username", unique = true, nullable = false)
    private String username;
    @NotBlank
    @Column(name = "password", nullable = false)
    private String password;

    @AttributeOverrides({
            @AttributeOverride(name = "firstName", column = @Column(name = "first_name", nullable = false)),
            @AttributeOverride(name = "lastName", column = @Column(name = "last_name", nullable = false)),
            @AttributeOverride(name = "birthDate", column = @Column(name = "birth_date")),
            @AttributeOverride(name = "country", column = @Column(name = "country"))
    })
    private PersonInfo personInfo;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    Collection<Subscription> subscriptions = new ArrayList<>();
}
