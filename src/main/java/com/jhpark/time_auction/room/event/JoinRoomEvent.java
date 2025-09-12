package com.jhpark.time_auction.room.event;

import java.time.LocalDateTime;

import com.jhpark.time_auction.common.ws.event.ServerEvent;
import com.jhpark.time_auction.common.ws.event.ServerEventType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinRoomEvent extends ServerEvent {
    private String roomId;
    private String sessionId; 

    public JoinRoomEvent(
        ServerEventType type,
        LocalDateTime sentAt,
        String roomId,
        String sessionId
    ){
        super(type, sentAt);
        this.roomId = roomId;
        this.sessionId = sessionId;
    }
}
