package com.jhpark.time_auction.common.ws.model.out;

import java.time.LocalDateTime;

import com.jhpark.time_auction.common.ws.model.ServerEventType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public abstract class ServerEvent {
    private LocalDateTime sentAt;
    
    public ServerEvent( LocalDateTime sentAt) {
        this.type = type;
        this.sentAt = sentAt;
    }
    
    // @Getter @Setter
    // public static class PongEvent extends ServerEvent {
    //     public PongEvent(LocalDateTime sentAt) {
    //         super(ServerEventType.PONG, sentAt);
    //     }
    // }
    
    @Getter @Setter @NoArgsConstructor
    public static class JoinRoomEvent extends ServerEvent {
        private String roomId;
        private boolean success;
        
        public JoinConfirmEvent(String roomId, boolean success, LocalDateTime sentAt) {
            super(ServerEventType.JOIN_CONFIRM, sentAt);
            this.roomId = roomId;
            this.success = success;
        }
    }
    
    // // 나머지 ServerEventType에 대한 클래스들도 위와 같은 패턴으로 구현하면 됩니다.
    // @Getter @Setter @NoArgsConstructor
    // public static class LeaveConfirmEvent extends ServerEvent {
    //     private String roomId;
    //     private boolean success;
        
    //     public LeaveConfirmEvent(String roomId, boolean success, LocalDateTime sentAt) {
    //         super(ServerEventType.LEAVE_CONFIRM, sentAt);
    //         this.roomId = roomId;
    //         this.success = success;
    //     }
    // }

    // @Getter @Setter @NoArgsConstructor
    // public static class ReadyConfirmEvent extends ServerEvent {
    //     private String roomId;
    //     private boolean success;
        
    //     public ReadyConfirmEvent(String roomId, boolean success, LocalDateTime sentAt) {
    //         super(ServerEventType.READY_CONFIRM, sentAt);
    //         this.roomId = roomId;
    //         this.success = success;
    //     }
    // }

    // @Getter @Setter @NoArgsConstructor
    // public static class NotReadyConfirmEvent extends ServerEvent {
    //     private String roomId;
    //     private boolean success;
        
    //     public NotReadyConfirmEvent(String roomId, boolean success, LocalDateTime sentAt) {
    //         super(ServerEventType.NOT_READY_CONFIRM, sentAt);
    //         this.roomId = roomId;
    //         this.success = success;
    //     }
    // }

    // @Getter @Setter @NoArgsConstructor
    // public static class GameStartConfirmEvent extends ServerEvent {
    //     private String roomId;
    //     private boolean success;
        
    //     public GameStartConfirmEvent(String roomId, boolean success, LocalDateTime sentAt) {
    //         super(ServerEventType.GAME_START_CONFIRM, sentAt);
    //         this.roomId = roomId;
    //         this.success = success;
    //     }
    // }

    // @Getter @Setter @NoArgsConstructor
    // public static class GameEndConfirmEvent extends ServerEvent {
    //     private String roomId;
    //     private boolean success;
        
    //     public GameEndConfirmEvent(String roomId, boolean success, LocalDateTime sentAt) {
    //         super(ServerEventType.GAME_END_CONFIRM, sentAt);
    //         this.roomId = roomId;
    //         this.success = success;
    //     }
    // }

    // @Getter @Setter @NoArgsConstructor
    // public static class RoundInConfirmEvent extends ServerEvent {
    //     private String gameId;
    //     private String roundId;
    //     private boolean success;
        
    //     public RoundInConfirmEvent(String gameId, String roundId, boolean success, LocalDateTime sentAt) {
    //         super(ServerEventType.ROUND_IN_CONFIRM, sentAt);
    //         this.gameId = gameId;
    //         this.roundId = roundId;
    //         this.success = success;
    //     }
    // }

    // @Getter @Setter @NoArgsConstructor
    // public static class RoundOutConfirmEvent extends ServerEvent {
    //     private String gameId;
    //     private String roundId;
    //     private boolean success;
        
    //     public RoundOutConfirmEvent(String gameId, String roundId, boolean success, LocalDateTime sentAt) {
    //         super(ServerEventType.ROUND_OUT_CONFIRM, sentAt);
    //         this.gameId = gameId;
    //         this.roundId = roundId;
    //         this.success = success;
    //     }
    // }

    // @Getter @Setter @NoArgsConstructor
    // public static class TimeStartConfirmEvent extends ServerEvent {
    //     private String roomId;
    //     private String roundId;
    //     private boolean success;
        
    //     public TimeStartConfirmEvent(String roomId, String roundId, boolean success, LocalDateTime sentAt) {
    //         super(ServerEventType.TIME_START_CONFIRM, sentAt);
    //         this.roomId = roomId;
    //         this.roundId = roundId;
    //         this.success = success;
    //     }
    // }

    // @Getter @Setter @NoArgsConstructor
    // public static class TimeEndConfirmEvent extends ServerEvent {
    //     private String roomId;
    //     private String roundId;
    //     private boolean success;
        
    //     public TimeEndConfirmEvent(String roomId, String roundId, boolean success, LocalDateTime sentAt) {
    //         super(ServerEventType.TIME_END_CONFIRM, sentAt);
    //         this.roomId = roomId;
    //         this.roundId = roundId;
    //         this.success = success;
    //     }
    // }
    
    // @Getter @Setter @NoArgsConstructor
    // public static class ChatEvent extends ServerEvent {
    //     private String senderId;
    //     private String message;
    //     private String roomId;

    //     public ChatEvent(String senderId, String message, String roomId, LocalDateTime sentAt) {
    //         super(ServerEventType.CHAT, sentAt);
    //         this.senderId = senderId;
    //         this.message = message;
    //         this.roomId = roomId;
    //     }
    // }

    // @Getter @Setter @NoArgsConstructor
    // public static class NoticeMessageEvent extends ServerEvent {
    //     private String message;

    //     public NoticeMessageEvent(String message, LocalDateTime sentAt) {
    //         super(ServerEventType.NOTICE_MESSAGE, sentAt);
    //         this.message = message;
    //     }
    // }

    // @Getter @Setter @NoArgsConstructor
    // public static class PhaseChangedEvent extends ServerEvent {
    //     private String roomId;
    //     private String newPhase;
        
    //     public PhaseChangedEvent(String roomId, String newPhase, LocalDateTime sentAt) {
    //         super(ServerEventType.PHASE_CHANGED, sentAt);
    //         this.roomId = roomId;
    //         this.newPhase = newPhase;
    //     }
    // }

    // /* Broadcast Events */
    // @Getter @Setter @NoArgsConstructor
    // public static class UserJoinedBroadcastEvent extends ServerEvent {
    //     private com.jhpark.time_auction.room.model.RoomEntry userEntry;
    //     public UserJoinedBroadcastEvent(com.jhpark.time_auction.room.model.RoomEntry userEntry, LocalDateTime sentAt) { 
    //         super(ServerEventType.USER_JOINED_BROADCAST, sentAt); 
    //         this.userEntry = userEntry; 
    //     }
    // }

    // @Getter @Setter @NoArgsConstructor
    // public static class UserLeftBroadcastEvent extends ServerEvent {
    //     private com.jhpark.time_auction.room.model.RoomEntry userEntry;
    //     public UserLeftBroadcastEvent(com.jhpark.time_auction.room.model.RoomEntry userEntry, LocalDateTime sentAt) { 
    //         super(ServerEventType.USER_LEFT_BROADCAST, sentAt); 
    //         this.userEntry = userEntry; 
    //     }
    // }

    // @Getter @Setter @NoArgsConstructor
    // public static class ReadyStatusBroadcastEvent extends ServerEvent {
    //     private com.jhpark.time_auction.room.model.RoomEntry userEntry;
    //     public ReadyStatusBroadcastEvent(com.jhpark.time_auction.room.model.RoomEntry userEntry, LocalDateTime sentAt) { 
    //         super(ServerEventType.READY_STATUS_BROADCAST, sentAt); 
    //         this.userEntry = userEntry; 
    //     }
    // }

    // @Getter @Setter @NoArgsConstructor
    // public static class GameStartBroadcastEvent extends ServerEvent {
    //     private com.jhpark.time_auction.game.model.Game game;
    //     public GameStartBroadcastEvent(com.jhpark.time_auction.game.model.Game game, LocalDateTime sentAt) { 
    //         super(ServerEventType.GAME_START_BROADCAST, sentAt); 
    //         this.game = game; 
    //     }
    // }

    // @Getter @Setter @NoArgsConstructor
    // public static class RoundStartBroadcastEvent extends ServerEvent {
    //     private com.jhpark.time_auction.game.model.Round round;
    //     public RoundStartBroadcastEvent(com.jhpark.time_auction.game.model.Round round, LocalDateTime sentAt) { 
    //         super(ServerEventType.ROUND_START_BROADCAST, sentAt); 
    //         this.round = round; 
    //     }
    // }

    // @Getter @Setter @NoArgsConstructor
    // public static class RoundEndBroadcastEvent extends ServerEvent {
    //     private java.util.List<com.jhpark.time_auction.record.model.RoundRecord> roundRecords;
    //     public RoundEndBroadcastEvent(java.util.List<com.jhpark.time_auction.record.model.RoundRecord> roundRecords, LocalDateTime sentAt) { 
    //         super(ServerEventType.ROUND_END_BROADCAST, sentAt); 
    //         this.roundRecords = roundRecords; 
    //     }
    // }

    // @Getter @Setter @NoArgsConstructor
    // public static class GameEndBroadcastEvent extends ServerEvent {
    //     private com.jhpark.time_auction.record.model.TimeWallet winnerWallet;
    //     public GameEndBroadcastEvent(com.jhpark.time_auction.record.model.TimeWallet winnerWallet, LocalDateTime sentAt) { 
    //         super(ServerEventType.GAME_END_BROADCAST, sentAt); 
    //         this.winnerWallet = winnerWallet; 
    //     }
    // }
}