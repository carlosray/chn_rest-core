package ru.vas.restcore.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.vas.restcore.db.domain.TestEn;
import ru.vas.restcore.db.repo.TestEnRepo;

import java.util.List;

@RequestMapping(
        produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequiredArgsConstructor
@Validated
public class FrontController {
    private final TestEnRepo repoTest;

    @DeleteMapping("test/{id}")
    public ResponseEntity<Void> deleteTest(@PathVariable Long id) {
        repoTest.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("test")
    public ResponseEntity<List<TestEn>> getTestJson() throws InterruptedException {
        final List<TestEn> all = repoTest.findAll();
        Thread.sleep(10000);
        return ResponseEntity.ok(all);
    }

    @PostMapping(value = "test", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TestEn> saveTest(@RequestBody TestEn testEn) {
        return ResponseEntity.ok(repoTest.save(testEn));
    }
}
