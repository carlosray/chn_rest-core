package ru.vas.restcore.service.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.vas.restcore.db.domain.UserEntity;
import ru.vas.restcore.service.SecurityService;

import java.util.Optional;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Override
    public UserEntity currentUser() {
        return (UserEntity) Optional.ofNullable(SecurityContextHolder.getContext())
                .flatMap(securityContext -> Optional.ofNullable(securityContext.getAuthentication()))
                .flatMap(authentication -> Optional.ofNullable(authentication.getPrincipal()))
                .filter(principal -> principal instanceof UserEntity)
                .orElse(null);
    }
}
