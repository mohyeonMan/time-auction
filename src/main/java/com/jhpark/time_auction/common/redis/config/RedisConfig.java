package com.jhpark.time_auction.common.redis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.RedisKeyValueAdapter.EnableKeyspaceEvents;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories(
    basePackages = "com.jhpark.time_auction",
    enableKeyspaceEvents = EnableKeyspaceEvents.ON_DEMAND
    
)
public class RedisConfig {

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // host/port는 application.yml의 spring.data.redis.* 를 따릅니다.
        return new LettuceConnectionFactory();
    }


    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // 날짜를 타임스탬프 대신 문자열로 직렬화
        mapper.registerModule(new JavaTimeModule()); // LocalDateTime 같은 자바8 날짜/시간 모듈 등록
        return mapper;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(
        RedisConnectionFactory connectionFactory,
        ObjectMapper objectMapper
    ) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        
        // Key 직렬화 (String)
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        
        // Value 직렬화 (JSON)
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        
        // Hash Key 직렬화 (String)
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        
        // Hash Value 직렬화 (JSON)
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        
        return redisTemplate;
    }

}