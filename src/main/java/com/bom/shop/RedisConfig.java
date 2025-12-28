package com.bom.shop;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    // 1. Lettuce 연결 (기본 캐싱용)
    @Bean
    public RedisConnectionFactory connectionFactory() {
        return new LettuceConnectionFactory(); // 기본 포트 6379 연결
    }

    // 2. RedisTemplate (Lettuce 기반의 데이터 저장소)
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory());

        // 키는 사람이 읽을 수 있게 String으로 설정 (이게 디버깅할 때 편해요!)
        template.setKeySerializer(new StringRedisSerializer());
        return template;
    }

    // 3. RedissonClient (락 구현용)
    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        // Redis 주소 설정 (로컬 기준)
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        return Redisson.create(config);
    }
}