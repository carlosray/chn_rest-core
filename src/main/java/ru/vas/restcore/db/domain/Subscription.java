package ru.vas.restcore.db.domain;

import lombok.*;
import ru.vas.restcore.db.domain.base.BaseTimestampEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
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

    private Type type;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    public enum Type {
        IP,
        DOMAIN
    }

    public enum Notification {
        MAIL,
        TELEGRAM
    }

}
