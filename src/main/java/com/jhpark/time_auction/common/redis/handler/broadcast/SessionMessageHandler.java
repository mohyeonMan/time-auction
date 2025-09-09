// package com.jhpark.time_auction.common.redis.handler.broadcast;
// import org.springframework.stereotype.Component;

// import com.jhpark.time_auction.common.redis.handler.RedisSessionManager;
// import com.jhpark.time_auction.common.ws.handler.BroadcastMessageHandler;
// import com.jhpark.time_auction.common.ws.handler.SessionManager;
// import com.jhpark.time_auction.common.ws.model.AbstractMessage;

// import lombok.extern.slf4j.Slf4j;

// @Slf4j
// @Component
// public class SessionMessageHandler implements BroadcastMessageHandler {

//     private final SessionManager sessionManager;

//     public SessionMessageHandler(RedisSessionManager sessionManager) {
//         this.sessionManager = sessionManager;
//     }

//     @Override
//     public boolean supports(String channel) {
//         // 이 핸들러는 ws:node:*:session:* 패턴을 지원합니다.
//         return channel.matches("ws:node:[^:]+:session:[^:]+");
//     }

//     @Override
//     public void handle(String channel, AbstractMessage message) {
//         String[] parts = channel.split(":");
//         String sessionKey = parts[parts.length - 1];
//         try {
//             // Redis에서 조회한 세션 ID에 해당하는 클라이언트로 메시지를 보냅니다.
//             sessionManager.sendToSession(sessionKey, message);
//         } catch (Exception e) {
//             log.error("Error sending message to session: sessionKey=" + sessionKey);
//             throw new RuntimeException(e);
//         }
//     }
// }