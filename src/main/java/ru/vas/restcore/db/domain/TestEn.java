package ru.vas.restcore.db.domain;

import lombok.*;
import ru.vas.restcore.db.domain.base.BaseTimestampEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "test_entity")
public class TestEn extends BaseTimestampEntity {
    @Column(name = "name")
    private String name;
    @Column(name = "ip")
    private String ip;
    @Column(name = "description")
    private String description;
}
