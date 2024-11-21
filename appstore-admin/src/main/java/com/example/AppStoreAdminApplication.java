package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EnableFeignClients
@EnableElasticsearchRepositories
@SpringBootApplication
public class AppStoreAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(AppStoreAdminApplication.class, args);
    }
}