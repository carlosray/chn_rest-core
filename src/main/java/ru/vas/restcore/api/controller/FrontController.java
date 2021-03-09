package ru.vas.restcore.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.vas.restcore.api.dto.UserInfoDTO;
import ru.vas.restcore.db.domain.TestEn;
import ru.vas.restcore.db.repo.TestEnRepo;
import ru.vas.restcore.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RequestMapping(
        value = "api",
        produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequiredArgsConstructor
@Validated
public class FrontController {
    private final TestEnRepo repoTest;
    private final UserService userService;

    @DeleteMapping("subscription/{id}")
    public ResponseEntity<Void> deleteTest(@PathVariable Long id) {
        repoTest.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("subscription")
    public ResponseEntity<List<TestEn>> getTestJson() throws InterruptedException {
        final List<TestEn> all = repoTest.findAll();
        return ResponseEntity.ok(all);
    }

    @PostMapping(value = "subscription", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TestEn> saveTest(@RequestBody TestEn testEn) {
        return ResponseEntity.ok(repoTest.save(testEn));
    }

    @GetMapping("user")
    public ResponseEntity<UserInfoDTO> getUserInfo() {
        return ResponseEntity.ok(userService.getUserInfo());
    }

    @PostMapping("user")
    public ResponseEntity<Void> saveUserInfo(@Valid @RequestBody UserInfoDTO userInfoDTO) {
        userService.saveUserInfo(userInfoDTO);
        return ResponseEntity.ok().build();
    }
}
