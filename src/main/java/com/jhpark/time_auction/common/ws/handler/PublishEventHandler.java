package com.jhpark.time_auction.common.ws.handler;

import java.util.Set;

import org.springframework.web.socket.WebSocketSession;

import com.jhpark.time_auction.common.ws.model.in.ClientEvent;
import com.jhpark.time_auction.common.ws.model.in.ClientEventType;

public interface PublishEventHandler {

    Set<ClientEventType> supports();

    void handle(WebSocketSession session, ClientEvent event);
}