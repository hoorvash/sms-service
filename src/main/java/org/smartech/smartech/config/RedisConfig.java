package org.smartech.smartech.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableRedisRepositories
public class RedisConfig {
    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(500);
        poolConfig.setMinIdle(200);
        poolConfig.setMaxIdle(50);
        JedisConnectionFactory jedis = new JedisConnectionFactory();
        jedis.setHostName("localhost");
        jedis.setPort(6363);
        jedis.setUsePool(true);
        return jedis;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }
}
