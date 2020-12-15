package ru.vas.restcore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class RestCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestCoreApplication.class, args);
    }

}
