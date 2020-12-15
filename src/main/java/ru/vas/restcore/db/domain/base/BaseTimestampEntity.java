package ru.vas.restcore.db.domain.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"creationDate", "createdBy", "modifiedDate", "modifiedBy"})
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimestampEntity extends BaseIdEntity {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(name = "last_modified_date", updatable = false)
    private Date creationDate;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    @Column(name = "modified_date")
    private Date modifiedDate;

    @LastModifiedBy
    @Column(name = "modified_by")
    private String modifiedBy;
}
