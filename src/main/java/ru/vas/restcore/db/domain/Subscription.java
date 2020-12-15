package ru.vas.restcore.db.domain;

import lombok.*;
import ru.vas.restcore.db.domain.base.BaseTimestampEntity;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "subscription")
public class Subscription extends BaseTimestampEntity {
    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

}
