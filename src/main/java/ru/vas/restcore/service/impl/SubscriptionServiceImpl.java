package ru.vas.restcore.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import ru.vas.restcore.api.dto.SubscriptionDTO;
import ru.vas.restcore.db.domain.Subscription;
import ru.vas.restcore.db.repo.SubscriptionRepository;
import ru.vas.restcore.exception.MaxCountSubscriptions;
import ru.vas.restcore.service.NotificationServiceClient;
import ru.vas.restcore.service.SecurityService;
import ru.vas.restcore.service.SubscriptionService;

import javax.annotation.PostConstruct;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final SecurityService securityService;
    private final NotificationServiceClient notificationServiceClient;
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
                .map(subscription -> new SubscriptionDTO(subscription, true))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<SubscriptionDTO> getAllSubsCurrentUser(Boolean withStatus) {
        final Set<SubscriptionDTO> subs = securityService.currentUser().getSubscriptions().stream()
                .map(SubscriptionDTO::new)
                .collect(Collectors.toSet());
        return withStatus ? notificationServiceClient.checkStatus(subs) : subs;
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
