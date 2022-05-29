package com.github.lwb_2021.user_center.config;

import com.github.lwb_2021.user_center.model.Account;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;


@Configuration
@EnableCaching
public class RedisConfig {
    @Bean
    public static RedisTemplate<String, Account> redisTemplateFactory(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Account> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer<Account> jackson2JsonRedisSerializer =
                new Jackson2JsonRedisSerializer<>(Account.class);
        redisTemplate.setDefaultSerializer(jackson2JsonRedisSerializer);

        return redisTemplate;
    }

}
