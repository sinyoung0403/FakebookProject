package com.example.fakebookproject.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

    // application-local.properties에서 Redis 호스트 정보 주입
    @Value("${spring.data.redis.host}")
    private String host;

    // application-local.properties에서 Redis 포트 정보 주입
    @Value("${spring.data.redis.port}")
    private int port;

    /**
     * Redis 연결을 위한 커넥션 팩토리 생성
     * Lettuce 클라이언트를 사용하여 Redis 서버와의 연결 설정
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    /**
     * RedisTemplate 설정
     * Redis에 데이터를 저장하거나 조회할 때 사용하는 템플릿 객체
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        return template;
    }

}
