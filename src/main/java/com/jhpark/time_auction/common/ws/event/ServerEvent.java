package com.jhpark.time_auction.common.ws.event;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jhpark.time_auction.common.ws.config.WebSocketConfig;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter 
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServerEvent {
    private final ServerEventType type;   // 예: ROOM_CREATED, PLAYER_READY ...
    private final Meta meta;          // 클라 보낸 시각(ms) - 있을 때만
    private final String nodeId;          // 발송 노드

    private final int version;            // 스키마 버전
    private final Object payload;              // 각 이벤트의 본문

    @Builder
    public ServerEvent(ServerEventType type,
                       String cid,
                       long clientAt,
                       Object payload) {
        this.type = type;
        this.meta = Meta.of(
                        cid, "s_" + UUID.randomUUID().toString().substring(0, 8),
                        clientAt);
        this.nodeId = WebSocketConfig.NodeId.ID;
        this.version = 1;
        this.payload = payload;
    }
}
