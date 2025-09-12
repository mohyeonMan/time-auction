package com.jhpark.time_auction.common.ws.event;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;
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
    private final String cid;             // client event id (x-msg-id)
    private final String sid;             // server event id
    private final long serverAt;          // 서버 생성 시각(ms)
    private final Long clientAt;          // 클라 보낸 시각(ms) - 있을 때만
    private final String nodeId;          // 발송 노드

    private final int version;            // 스키마 버전
    private final Object payload;              // 각 이벤트의 본문

    @Builder
    public ServerEvent(ServerEventType type,
                       String cid,
                       Long clientAt,
                       Object payload) {
        this.type = Objects.requireNonNull(type);
        this.cid = cid;
        this.clientAt = clientAt;
        this.sid = "s_" + UUID.randomUUID().toString().substring(0, 8);
        this.serverAt = Instant.now().toEpochMilli();
        this.nodeId = WebSocketConfig.NodeId.ID;
        this.version = 1;
        this.payload = payload;
    }
}
