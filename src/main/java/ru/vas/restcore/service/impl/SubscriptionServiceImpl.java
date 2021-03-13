package ru.vas.restcore.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vas.restcore.api.dto.SubscriptionDTO;
import ru.vas.restcore.db.domain.Subscription;
import ru.vas.restcore.db.repo.SubscriptionRepository;
import ru.vas.restcore.service.DataServiceFeign;
import ru.vas.restcore.service.SecurityService;
import ru.vas.restcore.service.SubscriptionService;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final SecurityService securityService;
    private final DataServiceFeign dataServiceFeign;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void postConstruct() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public Set<SubscriptionDTO> getAllSubs() {
        return subscriptionRepository.findAll().stream()
                .map(SubscriptionDTO::new)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<SubscriptionDTO> getAllSubsCurrentUser(Boolean withStatus) {
        final Set<Subscription> subscriptions = securityService.currentUser().getSubscriptions();

        final CompletableFuture<Map<String, Boolean>> statusesTask = CompletableFuture
                .supplyAsync(() -> withStatus ? this.getStatuses(subscriptions) : new HashMap<String, Boolean>())
                .exceptionally(ex -> {
                    log.error("Ошибка при получении статусов от data-service", ex);
                    return new HashMap<>();
                });

        final Set<SubscriptionDTO> result = subscriptions.stream()
                .map(SubscriptionDTO::new)
                .collect(Collectors.toSet());

        if (withStatus) {
            final Map<String, Boolean> statuses = statusesTask.join();
            result.forEach(sub -> sub.setStatus(statuses.getOrDefault(sub.getValue(), false)));
        }

        return result;
    }

    private Map<String, Boolean> getStatuses(Set<Subscription> subscriptions) {
        return dataServiceFeign.checkStatus(subscriptions.stream()
                .map(Subscription::getValue)
                .collect(Collectors.toSet()));
    }

    @Override
    public void deleteSubById(Long id) {
        subscriptionRepository.deleteById(id);
    }

    @Override
    public SubscriptionDTO createNewSub(SubscriptionDTO dto) {
        final Subscription subscription = objectMapper.convertValue(dto, Subscription.class);
        subscription.setUser(securityService.currentUser());
        return new SubscriptionDTO(subscriptionRepository.save(subscription));
    }
}
