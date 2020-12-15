package ru.vas.restcore.db.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Past;
import java.util.Date;

@Data
@With
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class PersonInfo {
    @Max(255)
    @Column(name = "firstName")
    private String firstName;

    @Max(255)
    @Column(name = "lastName")
    private String lastName;

    @Past
    @Column(name = "birthDate")
    private Date birthDate;

    @Max(255)
    @Column(name = "country")
    private String country;
}
