package ru.vas.restcore.db.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vas.restcore.db.domain.LocaleMessage;

import java.util.Locale;
import java.util.Optional;

@Repository
public interface LocaleMessageRepository extends JpaRepository<LocaleMessage, Long> {
    Optional<LocaleMessage> findByKeyAndLocale(String key, Locale locale);
}
