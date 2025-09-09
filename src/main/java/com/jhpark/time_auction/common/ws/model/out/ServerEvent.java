package com.jhpark.time_auction.common.ws.model.out;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.jhpark.time_auction.common.ws.model.ServerEventType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type",
    visible = true
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = ServerEvent.PongEvent.class, name = "PONG"),
    @JsonSubTypes.Type(value = ServerEvent.JoinConfirmEvent.class, name = "JOIN_CONFIRM"),
    @JsonSubTypes.Type(value = ServerEvent.LeaveConfirmEvent.class, name = "LEAVE_CONFIRM"),
    @JsonSubTypes.Type(value = ServerEvent.ReadyConfirmEvent.class, name = "READY_CONFIRM"),
    @JsonSubTypes.Type(value = ServerEvent.NotReadyConfirmEvent.class, name = "NOT_READY_CONFIRM"),
    @JsonSubTypes.Type(value = ServerEvent.GameStartConfirmEvent.class, name = "GAME_START_CONFIRM"),
    @JsonSubTypes.Type(value = ServerEvent.GameEndConfirmEvent.class, name = "GAME_END_CONFIRM"),
    @JsonSubTypes.Type(value = ServerEvent.RoundInConfirmEvent.class, name = "ROUND_IN_CONFIRM"),
    @JsonSubTypes.Type(value = ServerEvent.RoundOutConfirmEvent.class, name = "ROUND_OUT_CONFIRM"),
    @JsonSubTypes.Type(value = ServerEvent.TimeStartConfirmEvent.class, name = "TIME_START_CONFIRM"),
    @JsonSubTypes.Type(value = ServerEvent.TimeEndConfirmEvent.class, name = "TIME_END_CONFIRM"),
    @JsonSubTypes.Type(value = ServerEvent.ChatEvent.class, name = "CHAT"),
    @JsonSubTypes.Type(value = ServerEvent.NoticeMessageEvent.class, name = "NOTICE_MESSAGE"),
    @JsonSubTypes.Type(value = ServerEvent.PhaseChangedEvent.class, name = "PHASE_CHANGED")
})
public abstract class ServerEvent {
    private ServerEventType type;
    private LocalDateTime sentAt;
    
    public ServerEvent(ServerEventType type, LocalDateTime sentAt) {
        this.type = type;
    }
    
    @Getter @Setter
    public static class PongEvent extends ServerEvent {
        public PongEvent(LocalDateTime sentAt) {
            super(ServerEventType.PONG, sentAt);
        }
    }
    
    @Getter @Setter @NoArgsConstructor
    public static class JoinConfirmEvent extends ServerEvent {
        private String roomId;
        private boolean success;
        
        public JoinConfirmEvent(String roomId, boolean success, LocalDateTime sentAt) {
            super(ServerEventType.JOIN_CONFIRM, sentAt);
            this.roomId = roomId;
            this.success = success;
        }
    }
    
    // 나머지 ServerEventType에 대한 클래스들도 위와 같은 패턴으로 구현하면 됩니다.
    @Getter @Setter @NoArgsConstructor
    public static class LeaveConfirmEvent extends ServerEvent {
        private String roomId;
        private boolean success;
        
        public LeaveConfirmEvent(String roomId, boolean success, LocalDateTime sentAt) {
            super(ServerEventType.LEAVE_CONFIRM, sentAt);
            this.roomId = roomId;
            this.success = success;
        }
    }

    @Getter @Setter @NoArgsConstructor
    public static class ReadyConfirmEvent extends ServerEvent {
        private String roomId;
        private boolean success;
        
        public ReadyConfirmEvent(String roomId, boolean success, LocalDateTime sentAt) {
            super(ServerEventType.READY_CONFIRM, sentAt);
            this.roomId = roomId;
            this.success = success;
        }
    }

    @Getter @Setter @NoArgsConstructor
    public static class NotReadyConfirmEvent extends ServerEvent {
        private String roomId;
        private boolean success;
        
        public NotReadyConfirmEvent(String roomId, boolean success, LocalDateTime sentAt) {
            super(ServerEventType.NOT_READY_CONFIRM, sentAt);
            this.roomId = roomId;
            this.success = success;
        }
    }

    @Getter @Setter @NoArgsConstructor
    public static class GameStartConfirmEvent extends ServerEvent {
        private String roomId;
        private boolean success;
        
        public GameStartConfirmEvent(String roomId, boolean success, LocalDateTime sentAt) {
            super(ServerEventType.GAME_START_CONFIRM, sentAt);
            this.roomId = roomId;
            this.success = success;
        }
    }

    @Getter @Setter @NoArgsConstructor
    public static class GameEndConfirmEvent extends ServerEvent {
        private String roomId;
        private boolean success;
        
        public GameEndConfirmEvent(String roomId, boolean success, LocalDateTime sentAt) {
            super(ServerEventType.GAME_END_CONFIRM, sentAt);
            this.roomId = roomId;
            this.success = success;
        }
    }

    @Getter @Setter @NoArgsConstructor
    public static class RoundInConfirmEvent extends ServerEvent {
        private String gameId;
        private String roundId;
        private boolean success;
        
        public RoundInConfirmEvent(String gameId, String roundId, boolean success, LocalDateTime sentAt) {
            super(ServerEventType.ROUND_IN_CONFIRM, sentAt);
            this.gameId = gameId;
            this.roundId = roundId;
            this.success = success;
        }
    }

    @Getter @Setter @NoArgsConstructor
    public static class RoundOutConfirmEvent extends ServerEvent {
        private String gameId;
        private String roundId;
        private boolean success;
        
        public RoundOutConfirmEvent(String gameId, String roundId, boolean success, LocalDateTime sentAt) {
            super(ServerEventType.ROUND_OUT_CONFIRM, sentAt);
            this.gameId = gameId;
            this.roundId = roundId;
            this.success = success;
        }
    }

    @Getter @Setter @NoArgsConstructor
    public static class TimeStartConfirmEvent extends ServerEvent {
        private String roomId;
        private String roundId;
        private boolean success;
        
        public TimeStartConfirmEvent(String roomId, String roundId, boolean success, LocalDateTime sentAt) {
            super(ServerEventType.TIME_START_CONFIRM, sentAt);
            this.roomId = roomId;
            this.roundId = roundId;
            this.success = success;
        }
    }

    @Getter @Setter @NoArgsConstructor
    public static class TimeEndConfirmEvent extends ServerEvent {
        private String roomId;
        private String roundId;
        private boolean success;
        
        public TimeEndConfirmEvent(String roomId, String roundId, boolean success, LocalDateTime sentAt) {
            super(ServerEventType.TIME_END_CONFIRM, sentAt);
            this.roomId = roomId;
            this.roundId = roundId;
            this.success = success;
        }
    }
    
    @Getter @Setter @NoArgsConstructor
    public static class ChatEvent extends ServerEvent {
        private String senderId;
        private String message;
        private String roomId;

        public ChatEvent(String senderId, String message, String roomId, LocalDateTime sentAt) {
            super(ServerEventType.CHAT, sentAt);
            this.senderId = senderId;
            this.message = message;
            this.roomId = roomId;
        }
    }

    @Getter @Setter @NoArgsConstructor
    public static class NoticeMessageEvent extends ServerEvent {
        private String message;

        public NoticeMessageEvent(String message, LocalDateTime sentAt) {
            super(ServerEventType.NOTICE_MESSAGE, sentAt);
            this.message = message;
        }
    }

    @Getter @Setter @NoArgsConstructor
    public static class PhaseChangedEvent extends ServerEvent {
        private String roomId;
        private String newPhase;
        
        public PhaseChangedEvent(String roomId, String newPhase, LocalDateTime sentAt) {
            super(ServerEventType.PHASE_CHANGED, sentAt);
            this.roomId = roomId;
            this.newPhase = newPhase;
        }
    }
}