// package com.jhpark.time_auction.common.redis.handler.broadcast;

// import org.springframework.stereotype.Component;

// import com.jhpark.time_auction.common.ws.handler.BroadcastMessageHandler;
// import com.jhpark.time_auction.common.ws.model.AbstractMessage;

// @Component
// public class RoomMessageHandler implements BroadcastMessageHandler {

//     @Override
//     public boolean supports(String channel) {
//         return channel.startsWith("ws:room:");
//     }

//     @Override
//     public void handle(String channel, AbstractMessage message) {
//         // 이 핸들러는 모든 노드가 구독하는 ws:room:* 채널 메시지를 처리합니다.
//         // 클러스터의 모든 노드에 해당 메시지를 전송합니다.
//         // 현재 구조에서는 RedisMessagePublisher가 이미 노드별로 메시지를 라우팅하고 있으므로,
//         // 이 핸들러의 역할은 다른 목적으로 사용될 수 있습니다. (예: 모든 노드의 로그 기록)
//         // 여기서는 예시를 위해 단순 출력만 남겨둡니다.
//         System.out.println("Handling room broadcast for " + channel);
//     }
// }