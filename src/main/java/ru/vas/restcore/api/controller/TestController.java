package ru.vas.restcore.api.controller;

import lombok.RequiredArgsConstructor;
import org.jasypt.util.password.PasswordEncryptor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.vas.restcore.api.dto.UserDTO;
import ru.vas.restcore.db.repo.UserRepository;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("test")
@RequiredArgsConstructor
@Validated
public class TestController {
    private final PasswordEncryptor passwordEncryptor;
    private final UserRepository userRepository;
    private final MessageSource messageSource;

    @GetMapping("string")
    public ResponseEntity<String> getTestString() {
        return ResponseEntity.ok(passwordEncryptor.encryptPassword("test"));
    }

    @GetMapping("throw")
    public ResponseEntity<Void> throwing() throws Exception {
        throw new RuntimeException();
    }

    @GetMapping("localized")
    public ResponseEntity<String> localized() {
        String text = messageSource.getMessage("test.message", null, Locale.ENGLISH);
        return ResponseEntity.ok(text);
    }

    @PostMapping("save")
    public ResponseEntity<Void> save(@RequestBody UserDTO user) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("json")
    public ResponseEntity<Map<String, String>> getTestJson() {
        Map<String, String> map = new HashMap<>();
        map.put("testKey1", "testValue1");
        map.put("testKey2", "testValue2");
        return ResponseEntity.ok(map);
    }
}
