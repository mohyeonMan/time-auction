package com.jhpark.time_auction.common.redis.handler.broadcast;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhpark.time_auction.common.redis.router.BroadcastEventHandlerRouter;
import com.jhpark.time_auction.common.ws.event.ServerEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RedisMessageSubscriber implements MessageListener {

    private final BroadcastEventHandlerRouter router;
    private final ObjectMapper objectMapper;

    public RedisMessageSubscriber(
        BroadcastEventHandlerRouter router,
        ObjectMapper objectMapper
    ) {
        this.objectMapper = objectMapper;
        this.router = router;
    }

    @Override
    public void onMessage(Message messageContext, byte[] pattern) {
        try {
            String channel = new String(pattern);
            String receivedMessage = new String(messageContext.getBody());
            ServerEvent message = objectMapper.readValue(receivedMessage, ServerEvent.class);
            
            router.route(channel, message);

        } catch (Exception e) {
            log.error("Error processing message: " + e.getMessage());
        }
    }
}