// package com.jhpark.time_auction.common.redis.router;

// import org.springframework.stereotype.Component;

// import com.jhpark.time_auction.common.ws.handler.BroadcastMessageHandler;
// import com.jhpark.time_auction.common.ws.model.AbstractMessage;

// import java.util.List;

// @Component
// public class BroadcastMessageHandlerRouter {
    
//     private final List<BroadcastMessageHandler> handlers;

//     public BroadcastMessageHandlerRouter(List<BroadcastMessageHandler> handlers) {
//         this.handlers = handlers;
//     }

//     public void route(String channel, AbstractMessage message) {
//         handlers.stream()
//                 .filter(handler -> handler.supports(channel))
//                 .findFirst()
//                 .ifPresent(handler -> handler.handle(channel, message));
//     }
// }