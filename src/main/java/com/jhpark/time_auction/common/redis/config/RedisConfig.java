package com.jhpark.time_auction.common.redis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jhpark.time_auction.common.redis.handler.broadcast.RedisMessageSubscriber;
import com.jhpark.time_auction.common.ws.config.WebSocketConfig.NodeId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableRedisRepositories(basePackages = "com.jhpark.time_auction")
public class RedisConfig {


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

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter) {
        
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        
        container.addMessageListener(listenerAdapter, new PatternTopic("ws:room:*"));
        container.addMessageListener(listenerAdapter, new PatternTopic("ws:node:"+ NodeId.ID+":session:*"));
        
        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(RedisMessageSubscriber subscriber) {
        // 메시지 리스너를 MessageListenerAdapter로 변환
        return new MessageListenerAdapter(subscriber, "onMessage");
    }

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10); // Adjust pool size as needed
        scheduler.setThreadNamePrefix("game-scheduler-");
        scheduler.initialize();
        return scheduler;
    }
}