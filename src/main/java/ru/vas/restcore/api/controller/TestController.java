package ru.vas.restcore.api.controller;

import lombok.RequiredArgsConstructor;
import org.jasypt.util.password.PasswordEncryptor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.vas.restcore.api.dto.UserDTO;
import ru.vas.restcore.db.domain.TestEn;
import ru.vas.restcore.db.repo.TestEnRepo;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("test")
@RequiredArgsConstructor
@Validated
public class TestController {
    private final PasswordEncryptor passwordEncryptor;
    private final MessageSource messageSource;
    private final TestEnRepo repoTest;

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

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteTest(@PathVariable Long id) {
        repoTest.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<TestEn>> getTestJson() {
        final List<TestEn> all = repoTest.findAll();
        return ResponseEntity.ok(all);
    }

    @PostMapping
    public ResponseEntity<TestEn> saveTest(@RequestBody TestEn testEn) {
        return ResponseEntity.ok(repoTest.save(testEn));
    }
}
