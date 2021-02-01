package ru.vas.restcore;

import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@RequiredArgsConstructor
public class RestCoreApplication {
    private final Flyway flyway;

    public static void main(String[] args) {
        SpringApplication.run(RestCoreApplication.class, args);
    }

    /**
     * Переопределить flyway initializer по умолчанию чтобы ничего не делать
     */
    @Bean
    FlywayMigrationInitializer flywayInitializer(Flyway flyway) {
        return new FlywayMigrationInitializer(flyway, (f) -> {
        });
    }

    /**
     * Запустить миграцию в самом конце после генерации DDL
     */
    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> flyway.migrate();
    }

}
