package ru.vas.restcore.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.vas.restcore.api.dto.BlockedResourceDTO;
import ru.vas.restcore.api.dto.SubscriptionDTO;
import ru.vas.restcore.api.dto.UserInfoDTO;
import ru.vas.restcore.db.domain.Subscription;
import ru.vas.restcore.exception.ApiException;
import ru.vas.restcore.service.DataServiceClient;
import ru.vas.restcore.service.SubscriptionService;
import ru.vas.restcore.service.UserService;

import javax.validation.Valid;
import java.util.Set;

@RequestMapping(
        value = "api",
        produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequiredArgsConstructor
@Validated
public class FrontController {
    private final UserService userService;
    private final SubscriptionService subscriptionService;
    private final DataServiceClient dataServiceClient;

    @DeleteMapping("subscription/{id}")
    public ResponseEntity<Void> deleteSub(@PathVariable Long id) {
        subscriptionService.deleteSubById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("subscription")
    public ResponseEntity<Set<SubscriptionDTO>> getSubs(@RequestParam(value = "withStatus", required = false, defaultValue = "false") Boolean withStatus) {
        return ResponseEntity.ok(subscriptionService.getAllSubsCurrentUser(withStatus));
    }

    @PostMapping(value = "subscription", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SubscriptionDTO> saveSub(@Valid @RequestBody SubscriptionDTO sub) {
        return ResponseEntity.ok(subscriptionService.createNewSub(sub));
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

    @GetMapping("search")
    public ResponseEntity<Set<BlockedResourceDTO>> search(@RequestParam("type") Subscription.Type type,
                                                          @RequestParam("value") String value) {
        switch (type) {
            case IP: return ResponseEntity.ok(dataServiceClient.searchIp(value, true));
            case DOMAIN: return ResponseEntity.ok(dataServiceClient.searchDomain(value, true));
            default: throw new ApiException("Нет параметров для поиска", HttpStatus.BAD_REQUEST) {};
        }
    }

    @GetMapping("count")
    public ResponseEntity<Long> countOfBlocked() {
        return ResponseEntity.ok(dataServiceClient.searchCountBlocked(true));
    }
}
