package com.jhpark.time_auction.common.ws.handler.publish;

public interface MessagePublisher<T> {
    void publish(String destination, T message);
    void publishToUser(String userId, String destination, T message);
}