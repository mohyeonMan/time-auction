package com.jhpark.time_auction.common.ws.model.in;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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
    @JsonSubTypes.Type(value = ClientEvent.PingEvent.class, name = "PING"),
    @JsonSubTypes.Type(value = ClientEvent.JoinEvent.class, name = "JOIN"),
    @JsonSubTypes.Type(value = ClientEvent.LeaveEvent.class, name = "LEAVE"),
    @JsonSubTypes.Type(value = ClientEvent.ReadyEvent.class, name = "READY"),
    @JsonSubTypes.Type(value = ClientEvent.NotReadyEvent.class, name = "NOT_READY"),
    @JsonSubTypes.Type(value = ClientEvent.GameStartEvent.class, name = "GAME_START"),
    @JsonSubTypes.Type(value = ClientEvent.GameEndEvent.class, name = "GAME_END"),
    @JsonSubTypes.Type(value = ClientEvent.RoundInEvent.class, name = "ROUND_IN"),
    @JsonSubTypes.Type(value = ClientEvent.RoundOutEvent.class, name = "ROUND_OUT"),
    @JsonSubTypes.Type(value = ClientEvent.TimeStartEvent.class, name = "TIME_START"),
    @JsonSubTypes.Type(value = ClientEvent.TimeEndEvent.class, name = "TIME_END"),
    @JsonSubTypes.Type(value = ClientEvent.ChatEvent.class, name = "CHAT")
})
public abstract class ClientEvent {
    private ClientEventType type;
    private LocalDateTime sentAt;

    public ClientEvent(ClientEventType type, LocalDateTime sentAt) {
        this.type = type;
        this.sentAt = sentAt;
    }

    /* 모든 하위 이벤트 클래스들을 public static 내부 클래스로 정의합니다. */

    @Getter
    @Setter
    @NoArgsConstructor
    public static class PingEvent extends ClientEvent {
        public PingEvent(LocalDateTime sentAt) {
            super(ClientEventType.PING, sentAt);
        }
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    public static class JoinEvent extends ClientEvent {
        private String roomId;
    
        public JoinEvent(String roomId, LocalDateTime sentAt) {
            super(ClientEventType.JOIN, sentAt);
            this.roomId = roomId;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class LeaveEvent extends ClientEvent {
        private String roomId;

        public LeaveEvent(String roomId, LocalDateTime sentAt) {
            super(ClientEventType.LEAVE, sentAt);
            this.roomId = roomId;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ReadyEvent extends ClientEvent {
        private String roomId;
    
        public ReadyEvent(String roomId, LocalDateTime sentAt) {
            super(ClientEventType.READY, sentAt);
            this.roomId = roomId;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class NotReadyEvent extends ClientEvent {
        private String roomId;
    
        public NotReadyEvent(String roomId, LocalDateTime sentAt) {
            super(ClientEventType.NOT_READY, sentAt);
            this.roomId = roomId;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class GameStartEvent extends ClientEvent {
        private String roomId;
    
        public GameStartEvent(String roomId, LocalDateTime sentAt) {
            super(ClientEventType.GAME_START, sentAt);
            this.roomId = roomId;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class GameEndEvent extends ClientEvent {
        private String gameId;

        public GameEndEvent(String gameId, LocalDateTime sentAt) {
            super(ClientEventType.GAME_END, sentAt);
            this.gameId = gameId;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RoundInEvent extends ClientEvent {
        private String gameId;
        private String roundId;

        public RoundInEvent(String gameId, String roundId, LocalDateTime sentAt) {
            super(ClientEventType.ROUND_IN, sentAt);
            this.gameId = gameId;
            this.roundId = roundId;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RoundOutEvent extends ClientEvent {
        private String gameId;
        private String roundId;

        public RoundOutEvent(String gameId, String roundId, LocalDateTime sentAt) {
            super(ClientEventType.ROUND_IN, sentAt);
            this.gameId = gameId;
            this.roundId = roundId;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class TimeStartEvent extends ClientEvent {
        private String roundId;

        public TimeStartEvent(String roundId, LocalDateTime sentAt) {
            super(ClientEventType.TIME_START, sentAt);
            this.roundId = roundId;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class TimeEndEvent extends ClientEvent {
        private String roundId;

        public TimeEndEvent(String roundId, LocalDateTime sentAt) {
            super(ClientEventType.TIME_START, sentAt);
            this.roundId = roundId;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ChatEvent extends ClientEvent {
        private String roomId;
        private String message;

        public ChatEvent(String roomId, String message, LocalDateTime sentAt) {
            super(ClientEventType.CHAT, sentAt);
            this.roomId = roomId;
            this.message = message;
        }
    }
}