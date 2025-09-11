// package com.jhpark.time_auction.common.redis.handler.publish;

// import java.util.Set;

// import org.springframework.stereotype.Service;
// import org.springframework.web.socket.WebSocketSession;

// import com.jhpark.time_auction.common.ws.handler.PublishEventHandler;
// import com.jhpark.time_auction.common.ws.handler.SessionManager;
// import com.jhpark.time_auction.common.ws.model.in.ClientEvent;
// import com.jhpark.time_auction.common.ws.model.in.ClientEventType;
// import com.jhpark.time_auction.common.ws.model.out.ServerEvent;

// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;

// @Slf4j
// @Service
// @RequiredArgsConstructor
// public class PingEventHandler implements PublishEventHandler {

//     private final SessionManager sessionManager;
    
//     @Override
//     public Set<ClientEventType> supports() {
//         return Set.of(ClientEventType.PING);
//     }

//     @Override
//     public void handle(WebSocketSession session, ClientEvent event) {
//         sessionManager.renewExpiration(session.getId());

//         sessionManager.sendToSession(session, new ServerEvent.PongEvent(event.getSentAt()));

//         log.info("PONG : {}", session.getId());
        
//     }
// }
