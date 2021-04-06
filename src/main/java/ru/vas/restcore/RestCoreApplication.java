package ru.vas.restcore;

import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@EnableEurekaClient
@EnableConfigurationProperties
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@RequiredArgsConstructor
@EnableFeignClients
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
