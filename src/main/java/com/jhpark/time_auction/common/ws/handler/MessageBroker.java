package com.jhpark.time_auction.common.ws.handler;

import com.jhpark.time_auction.common.ws.model.Message;

public interface MessageBroker {

    void publish(String topic, Message message);

    void subscribe(String topic);
    
}
