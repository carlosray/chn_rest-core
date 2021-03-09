package ru.vas.restcore.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vas.restcore.api.dto.SubscriptionDTO;
import ru.vas.restcore.db.domain.Subscription;
import ru.vas.restcore.db.repo.SubscriptionRepository;
import ru.vas.restcore.service.SecurityService;
import ru.vas.restcore.service.SubscriptionService;

import javax.annotation.PostConstruct;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final SecurityService securityService;
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
    public Set<SubscriptionDTO> getAllSubsCurrentUser() {
        return securityService.currentUser().getSubscriptions().stream()
                .map(SubscriptionDTO::new)
                .collect(Collectors.toSet());
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
