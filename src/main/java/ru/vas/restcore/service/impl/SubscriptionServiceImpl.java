package ru.vas.restcore.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.vas.restcore.api.dto.CheckStatusDTO;
import ru.vas.restcore.api.dto.SubscriptionDTO;
import ru.vas.restcore.db.domain.Subscription;
import ru.vas.restcore.db.repo.SubscriptionRepository;
import ru.vas.restcore.exception.ApiException;
import ru.vas.restcore.exception.MaxCountSubscriptions;
import ru.vas.restcore.service.DataServiceFeign;
import ru.vas.restcore.service.SecurityService;
import ru.vas.restcore.service.SubscriptionService;

import javax.annotation.PostConstruct;
import java.util.*;
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
    @Value("${subs.maximum-count:7}")
    private long maxSubsCount;

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

        final CompletableFuture<Set<CheckStatusDTO>> statusesTask = withStatus ? CompletableFuture
                .supplyAsync(() -> this.getStatuses(subscriptions))
                .exceptionally(ex -> {
                    log.error("Ошибка при получении статусов от data-service", ex);
                    return new HashSet<>();
                }) : null;

        final Set<SubscriptionDTO> result = subscriptions.stream()
                .map(SubscriptionDTO::new)
                .collect(Collectors.toSet());

        Optional.ofNullable(statusesTask)
                .map(CompletableFuture::join)
                .ifPresent(statuses -> result.forEach(sub -> sub.setStatus(statuses.contains(new CheckStatusDTO(sub)))));

        return result;
    }

    private Set<CheckStatusDTO> getStatuses(Set<Subscription> subscriptions) {
        return dataServiceFeign.checkStatus(subscriptions.stream()
                .map(CheckStatusDTO::new)
                .collect(Collectors.toSet()))
                .stream()
                .filter(CheckStatusDTO::getStatus)
                .collect(Collectors.toSet());
    }

    @Override
    public void deleteSubById(Long id) {
        subscriptionRepository.deleteById(id);
    }

    @Override
    public SubscriptionDTO createNewSub(SubscriptionDTO dto) {
        if (this.countSubsCurrentUser() + 1 > this.maxSubsCount) {
            throw new MaxCountSubscriptions(this.maxSubsCount);
        }
        final Subscription subscription = objectMapper.convertValue(dto, Subscription.class);
        subscription.setUser(securityService.currentUser());
        return new SubscriptionDTO(subscriptionRepository.save(subscription));
    }

    private long countSubsCurrentUser() {
        return subscriptionRepository.count(Example.of(new Subscription().withUser(securityService.currentUser())));
    }
}
