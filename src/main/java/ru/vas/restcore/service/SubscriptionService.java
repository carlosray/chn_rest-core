package ru.vas.restcore.service;

import org.springframework.security.access.prepost.PreAuthorize;
import ru.vas.restcore.api.dto.SubscriptionDTO;

import java.util.Set;

public interface SubscriptionService {
    Set<SubscriptionDTO> getAllSubs();
    Set<SubscriptionDTO> getAllSubsCurrentUser();
    @PreAuthorize("belongsToTheCurrentUser(#id)")
    void deleteSubById(Long id);
    SubscriptionDTO createNewSub(SubscriptionDTO dto);
}
