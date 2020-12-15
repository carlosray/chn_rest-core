package ru.vas.restcore.db.domain;

import lombok.*;
import ru.vas.restcore.db.domain.base.BaseTimestampEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.Locale;

@EqualsAndHashCode(callSuper = true)
@Data
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "locale_message",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"locale", "key"}, name = "uq_locale_message_locale_key")}
)
public class LocaleMessage extends BaseTimestampEntity {
    @Column(name = "locale", nullable = false, updatable = false)
    private Locale locale;
    @Column(name = "key", nullable = false, updatable = false)
    private String key;
    @Column(name = "message", nullable = false)
    private String message;

}
