package ru.vas.restcore.db.domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.vas.restcore.db.domain.base.BaseTimestampEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true, exclude = {"username", "password", "personInfo", "subscriptions", "roles"})
@Data
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user",
        indexes = @Index(name = "IDX_USER_USERNAME", columnList = "username"))
public class UserEntity extends BaseTimestampEntity implements UserDetails {
    @NotBlank
    @Column(name = "username", unique = true, nullable = false)
    private String username;
    @NotBlank
    @Column(name = "password", nullable = false)
    private String password;

    @AttributeOverrides({
            @AttributeOverride(name = "firstName", column = @Column(name = "first_name", nullable = false)),
            @AttributeOverride(name = "lastName", column = @Column(name = "last_name", nullable = false)),
            @AttributeOverride(name = "company", column = @Column(name = "company")),
            @AttributeOverride(name = "country", column = @Column(name = "country")),
            @AttributeOverride(name = "city", column = @Column(name = "city")),
            @AttributeOverride(name = "info", column = @Column(name = "info"))
    })
    private PersonInfo personInfo;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    Set<Subscription> subscriptions = new HashSet<>();

    @ManyToMany(targetEntity = RoleEntity.class)
    Set<RoleEntity> roles = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
