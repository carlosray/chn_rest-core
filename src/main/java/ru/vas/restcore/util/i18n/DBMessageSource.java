package ru.vas.restcore.util.i18n;

import lombok.RequiredArgsConstructor;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.stereotype.Component;
import ru.vas.restcore.db.repo.LocaleMessageRepository;

import java.text.MessageFormat;
import java.util.Locale;

@RequiredArgsConstructor
@Component("messageSource")
public class DBMessageSource extends AbstractMessageSource {
    private final LocaleMessageRepository localeMessageRepository;
    private static final Locale DEFAULT_LOCALE = Locale.getDefault();

    @Override
    protected MessageFormat resolveCode(String key, Locale locale) {
        return localeMessageRepository.findByKeyAndLocale(key, locale)
                .map(localeMessage -> new MessageFormat(localeMessage.getMessage(), locale))
                .orElseGet(() -> localeMessageRepository.findByKeyAndLocale(key, DEFAULT_LOCALE)
                        .map(localeMessage -> new MessageFormat(localeMessage.getMessage(), locale))
                        .orElse(null));
    }
}
