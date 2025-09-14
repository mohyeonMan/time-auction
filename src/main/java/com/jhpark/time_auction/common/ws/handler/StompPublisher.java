package com.jhpark.time_auction.common.ws.handler;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;


import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StompPublisher<T> implements MessagePublisher<T> {
    private final SimpMessagingTemplate template;

    @Override 
    public void publish(String destination, T message) {
        template.convertAndSend(destination, message);
    }

    @Override 
    public void publishToUser(String user, String destination, T message) {
        template.convertAndSendToUser(user, destination, message);
    }
}