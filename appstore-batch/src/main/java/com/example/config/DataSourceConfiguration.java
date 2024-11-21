package com.example.config;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DataSourceConfiguration {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.batch.hikari")
    public DataSource dataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean("serviceDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.service.hikari")
    public DataSource serviceDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

}
