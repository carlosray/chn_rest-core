package ru.vas.restcore.db.migration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import ru.vas.restcore.db.migration.util.MigrationConfig;
import ru.vas.restcore.db.migration.util.impl.JsonMigrationConfig;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class R__LocaleMessages extends BaseJavaMigration {
    private JdbcTemplate jdbcTemplate;
    private final static String INPUT_JSON_RESOURCE = "db/migration/locale_messages.json";
    private MigrationConfig<Set<LocalizedMessageResource>> config = null;
    private List<LocalizedMessage> allLocalizedMessagesFromDb;

    public R__LocaleMessages() throws IOException {
        this.config = new JsonMigrationConfig<>(INPUT_JSON_RESOURCE, new TypeReference<Set<LocalizedMessageResource>>() {
        });
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    final static class LocalizedMessageResource implements Serializable {
        @JsonProperty("alias")
        private String alias;
        @JsonProperty("messages")
        private Set<LocalizedMessage> localizedMessages;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @With
    @AllArgsConstructor
    @EqualsAndHashCode(exclude = {"message", "id"})
    final static class LocalizedMessage implements Serializable {
        @JsonProperty("locale")
        private Locale locale;
        @JsonProperty("message")
        private String message;
        @JsonIgnore
        private String alias;
        @JsonIgnore
        private Long id;
    }

    @Override
    public void migrate(Context context) throws Exception {
        jdbcTemplate = new JdbcTemplate(new SingleConnectionDataSource(context.getConnection(), true));
        Set<LocalizedMessage> localizedMessages = getLocalizedMessagesFromTemplate();

        localizedMessages.forEach(localizedMessage ->
        {
            Optional<LocalizedMessage> localizedMessageFromDb = getSameFromDb(localizedMessage);
            if (localizedMessageFromDb.isPresent()) {
                if (messagesNotEquals(localizedMessage, localizedMessageFromDb.get())) {
                    updateRow(localizedMessage, localizedMessageFromDb.get());
                }
            } else {
                insertRow(localizedMessage);
            }
        });
        log.info(this.getClass().getName() + " migration Successful");
    }

    private Set<LocalizedMessage> getLocalizedMessagesFromTemplate() {
        return config.getMappedResource().stream()
                .peek(localizedMessageResource -> localizedMessageResource.getLocalizedMessages()
                        .forEach(localizedMessage -> localizedMessage.setAlias(localizedMessageResource.getAlias())))
                .flatMap(localizedMessageResource -> localizedMessageResource.getLocalizedMessages().stream())
                .collect(Collectors.toSet());
    }

    private RowMapper<LocalizedMessage> mapRows() {
        return (resultSet, i) -> new LocalizedMessage()
                .withLocale(new Locale(resultSet.getString("locale")))
                .withMessage(resultSet.getString("message"))
                .withAlias(resultSet.getString("key"))
                .withId(resultSet.getLong("id"));
    }

    private Optional<LocalizedMessage> getSameFromDb(LocalizedMessage localizedMessage) {
        setUpAllFromDbIfNull();

        LocalizedMessage localizedMessageFromDb = null;
        int indexOf = allLocalizedMessagesFromDb.indexOf(localizedMessage);
        if (indexOf != -1) {
            localizedMessageFromDb = allLocalizedMessagesFromDb.get(indexOf);
        }
        return Optional.ofNullable(localizedMessageFromDb);
    }

    private void setUpAllFromDbIfNull() {
        if (Objects.isNull(allLocalizedMessagesFromDb)) {
            this.allLocalizedMessagesFromDb = getLocalizedMessagesFromDB();
        }
    }

    private List<LocalizedMessage> getLocalizedMessagesFromDB() {
        return jdbcTemplate.query("SELECT locale, key, message, id FROM checker.locale_message", mapRows());
    }

    private boolean messagesNotEquals(LocalizedMessage localizedMessage, LocalizedMessage localizedMessageFromDb) {
        return !localizedMessage.getMessage().equals(localizedMessageFromDb.getMessage());
    }

    private void updateRow(LocalizedMessage localizedMessage, LocalizedMessage localizedMessageFromDb) {
        jdbcTemplate.update("UPDATE checker.locale_message SET message = ? WHERE id = ?", localizedMessage.getMessage(), localizedMessageFromDb.getId());
    }

    private void insertRow(LocalizedMessage localizedMessage) {
        jdbcTemplate.update("INSERT INTO checker.locale_message (locale, key, message, id) VALUES (?, ?, ?, nextval('PK_ID_SEQUENCE'))",
                localizedMessage.getLocale().getLanguage(),
                localizedMessage.getAlias(),
                localizedMessage.getMessage()
        );
    }

    @Override
    public Integer getChecksum() {
        return config.getChecksum();
    }
}
