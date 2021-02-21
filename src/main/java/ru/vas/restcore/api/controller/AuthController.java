package ru.vas.restcore.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vas.restcore.api.dto.JwtTokenDTO;
import ru.vas.restcore.api.dto.UserLoginDTO;
import ru.vas.restcore.api.dto.UserRegisterDTO;
import ru.vas.restcore.db.domain.UserEntity;
import ru.vas.restcore.service.UserService;

import javax.validation.Valid;

@RequestMapping(
        produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping(value = "register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserEntity> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        return ResponseEntity.ok(userService.register(userRegisterDTO));
    }

    @PostMapping(value = "login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtTokenDTO> login(@RequestBody UserLoginDTO userLoginDTO) {
        return ResponseEntity.ok(userService.login(userLoginDTO.getUsername(), userLoginDTO.getPassword()));
    }
}
