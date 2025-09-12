package com.jhpark.time_auction.common.ws.event;

import java.time.LocalDateTime;
import java.util.UUID;

import com.jhpark.time_auction.common.ws.config.WebSocketConfig;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServerEvent {
    ServerEventType type;
    String eventId;
    LocalDateTime eventAt;
    LocalDateTime sentAt;
    String sentNodeId;

    public ServerEvent(
        ServerEventType type, 
        LocalDateTime sentAt
    ){
        this.type = type;
        this.eventId = "event_"+UUID.randomUUID().toString().substring(0, 8);
        this.eventAt = LocalDateTime.now();
        this.sentNodeId = WebSocketConfig.NodeId.ID;
    }
}
