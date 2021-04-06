package ru.vas.restcore.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vas.restcore.api.dto.SubscriptionDTO;
import ru.vas.restcore.service.SubscriptionService;

import java.util.Set;

@RestController
@RequestMapping("service")
@RequiredArgsConstructor
@Validated
public class NotificationController {
    private final SubscriptionService subscriptionService;

    @GetMapping("subs/all")
    public ResponseEntity<Set<SubscriptionDTO>> getAllSubscriptions() {
        return ResponseEntity.ok(subscriptionService.getAllSubs());
    }

}
