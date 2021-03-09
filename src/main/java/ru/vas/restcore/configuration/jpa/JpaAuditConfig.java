package ru.vas.restcore.configuration.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import ru.vas.restcore.db.domain.UserEntity;
import ru.vas.restcore.service.SecurityService;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@RequiredArgsConstructor
public class JpaAuditConfig {
    private final SecurityService securityService;

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.ofNullable(securityService.currentUser())
                .map(UserEntity::getUsername);
    }
}
