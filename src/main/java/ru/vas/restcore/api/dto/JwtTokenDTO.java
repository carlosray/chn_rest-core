package ru.vas.restcore.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class JwtTokenDTO implements Serializable {
    private String jwt;
}
