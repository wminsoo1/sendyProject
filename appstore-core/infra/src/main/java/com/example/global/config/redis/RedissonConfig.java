package com.example.global.config.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    private static final String REDISSON_HOST_PREFIX = "redis://";

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress(REDISSON_HOST_PREFIX + host + ":" + port)
            .setConnectionPoolSize(64)          // 연결 풀 크기
            .setConnectionMinimumIdleSize(64)   // 최소 유휴 연결 수
            .setTimeout(3000)                   // 명령 실행 타임아웃
            .setRetryAttempts(3)                // 명령 재시도 횟수
            .setRetryInterval(1500)             // 재시도 간격
            .setIdleConnectionTimeout(10000)    // 유휴 연결 타임아웃 최소와 최대 크키가 같아서 설정이 의미가 없음
            .setConnectTimeout(10000);           // 연결 타임아웃

        return Redisson.create(config);
    }
}