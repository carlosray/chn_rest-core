package ru.vas.restcore.db.domain;

import lombok.*;
import ru.vas.restcore.db.domain.base.BaseTimestampEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true, exclude = {"name", "description", "value", "type", "user"})
@Data
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "subscription")
public class Subscription extends BaseTimestampEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT", nullable = true)
    private String description;

    @Column(name = "value", nullable = false)
    @NotBlank
    private String value;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "notification", nullable = false)
    @Enumerated(EnumType.STRING)
    private Notification notification;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private UserEntity user;

    public enum Type {
        IP,
        DOMAIN
    }

    public enum Notification {
        MAIL
    }

}
