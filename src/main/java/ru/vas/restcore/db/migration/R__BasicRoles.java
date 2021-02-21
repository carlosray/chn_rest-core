package ru.vas.restcore.db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.util.Arrays;
import java.util.List;

public class R__BasicRoles extends BaseJavaMigration {
    private final List<String> roles = Arrays.asList("ROLE_ADMIN", "ROLE_USER");

    @Override
    public void migrate(Context context) throws Exception {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(new SingleConnectionDataSource(context.getConnection(), true));
        final List<String> rolesFromDb = jdbcTemplate.query("SELECT roleName FROM checker.role",
                (resultSet, i) -> resultSet.getString("roleName"));
        roles.stream()
                .filter(roleName -> !rolesFromDb.contains(roleName))
                .forEach(roleName -> {
                    jdbcTemplate.update("INSERT INTO checker.role (id, roleName) VALUES (nextval('PK_ID_SEQUENCE'), ?)", roleName);
                });
    }

    @Override
    public Integer getChecksum() {
        return 100;
    }
}
