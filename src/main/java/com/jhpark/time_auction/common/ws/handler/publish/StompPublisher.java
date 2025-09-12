package com.jhpark.time_auction.common.ws.handler.publish;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.jhpark.time_auction.common.ws.event.ServerEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StompPublisher implements MessagePublisher<ServerEvent> {
    private final SimpMessagingTemplate template;

    @Override 
    public void publish(String destination, ServerEvent message) {
        template.convertAndSend(destination, message);
    }

    @Override 
    public void publishToUser(String user, String destination, ServerEvent message) {
        template.convertAndSendToUser(user, destination, message);
    }
}