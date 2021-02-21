package ru.vas.restcore.db.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Max;

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

    @Max(255)
    @Column(name = "company")
    private String company;

    @Max(255)
    @Column(name = "country")
    private String country;

    @Max(255)
    @Column(name = "city")
    private String city;

    @Column(name = "info", columnDefinition = "TEXT")
    private String info;
}
