package org.smartech.configuration;

import org.smartech.smartech.config.RedisProperties;
import org.springframework.boot.test.context.TestConfiguration;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@TestConfiguration
public class TestRedisConfiguration {
    private RedisServer redisServer;

    public TestRedisConfiguration(RedisProperties redisProperties) {
//        this.redisServer = RedisServer.builder().port(redisProperties.getRedisPort()).setting("maxmemory 128M").build();
//        this.redisServer = new RedisServer(redisProperties.getRedisPort());
    }

    @PostConstruct
    public void postConstruct() {
//        redisServer = RedisServer.builder().port(6379).setting("maxmemory 128M").build();
//        redisServer.start();
    }

    @PreDestroy
    public void preDestroy() {
        redisServer.stop();
    }
}
