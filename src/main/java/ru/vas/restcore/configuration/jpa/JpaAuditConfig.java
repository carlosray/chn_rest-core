package ru.vas.restcore.configuration.jpa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.ofNullable(SecurityContextHolder.getContext())
                .flatMap(securityContext -> Optional.ofNullable(securityContext.getAuthentication()))
                .flatMap(authentication -> Optional.ofNullable(authentication.getPrincipal()))
                .filter(principal -> principal instanceof UserDetails)
                .map(principal -> (((UserDetails) principal).getUsername()));
    }
}
