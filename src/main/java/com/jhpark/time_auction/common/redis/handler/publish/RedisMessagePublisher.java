package com.jhpark.time_auction.common.redis.handler.publish;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.jhpark.time_auction.common.ws.handler.publish.MessagePublisher;
import com.jhpark.time_auction.common.ws.model.out.ServerEvent;

@Component
public class RedisMessagePublisher implements MessagePublisher<ServerEvent> {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisMessagePublisher(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void publish(String destination, ServerEvent event) {
        redisTemplate.convertAndSend(destination, event);
    }
    
}