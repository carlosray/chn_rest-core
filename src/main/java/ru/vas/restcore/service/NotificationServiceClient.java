package ru.vas.restcore.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import ru.vas.restcore.api.dto.SubscriptionDTO;

import java.util.Set;

@FeignClient("notification-service")
public interface NotificationServiceClient {

    @PostMapping("/api/statuses")
    Set<SubscriptionDTO> checkStatus(Set<SubscriptionDTO> ips);

}
