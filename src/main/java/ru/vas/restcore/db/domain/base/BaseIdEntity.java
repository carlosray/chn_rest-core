package ru.vas.restcore.db.domain.base;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public abstract class BaseIdEntity implements Serializable {
    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    @Column(name = "id")
    protected Long id;
}
