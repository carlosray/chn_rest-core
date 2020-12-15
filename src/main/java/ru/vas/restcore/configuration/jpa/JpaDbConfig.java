package ru.vas.restcore.configuration.jpa;

import lombok.Data;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.config.BootstrapMode;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        transactionManagerRef = "apiTransactionManager",
        entityManagerFactoryRef = "apiEntityManagerFactory",
        basePackages = "ru.vas.restcore.db",
        bootstrapMode = BootstrapMode.DEFERRED)
@EnableTransactionManagement
public class JpaDbConfig {

    @ConfigurationProperties(prefix = "api.datasource.hibernate")
    @Component
    @Data
    static class ApiDataSourceProperties {
        private Map<String, String> properties;
    }

    @ConfigurationProperties(prefix = "api.datasource")
    @Bean(name = "apiDataSourceProperties")
    @Primary
    public DataSourceProperties apiDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "apiDataSource")
    @Primary
    public DataSource dataSource(@Qualifier("apiDataSourceProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    @Bean(name = "apiEntityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("apiDataSource") DataSource dataSource, ApiDataSourceProperties properties) {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("ru.vas.restcore.db.domain");
        factory.setDataSource(dataSource);
        factory.setJpaPropertyMap(properties.getProperties());
        return factory;
    }

    @Bean(name = "apiTransactionManager")
    @Primary
    public PlatformTransactionManager transactionManager(@Qualifier("apiEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory);
        return txManager;
    }
}
