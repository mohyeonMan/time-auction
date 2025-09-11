// package com.jhpark.time_auction.room.handler;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.jhpark.time_auction.common.ws.handler.PublishEventHandler;
// import com.jhpark.time_auction.common.ws.handler.publish.MessagePublisher;
// import com.jhpark.time_auction.common.ws.model.in.ClientEvent;
// import com.jhpark.time_auction.common.ws.model.in.ClientEventType;
// import com.jhpark.time_auction.common.ws.model.out.ServerEvent;
// import com.jhpark.time_auction.room.model.RoomEntry;
// import com.jhpark.time_auction.room.service.RoomService;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.stereotype.Component;
// import org.springframework.web.socket.WebSocketSession;

// import java.io.IOException;
// import java.time.LocalDateTime;
// import java.util.Map;
// import java.util.Set;

// @Slf4j
// @Component
// @RequiredArgsConstructor
// public class RoomPublishEventHandler implements PublishEventHandler {

//     private final RoomService roomService;
//     private final MessagePublisher<ServerEvent> publisher;

//     @Override
//     public Set<ClientEventType> supports() {
//         return Set.of(ClientEventType.JOIN, ClientEventType.LEAVE, ClientEventType.READY, ClientEventType.NOT_READY);
//     }

//     @Override
//     public void handle(WebSocketSession session, ClientEvent event) {
//         String userId = session.getAttributes().get("HTTP_SESSION_ID").toString();
//         String roomId = null;

//         switch (event.getType()) {
//             case JOIN: {
//                 ClientEvent.JoinEvent joinEvent = (ClientEvent.JoinEvent) event;
//                 roomId = joinEvent.getRoomId();
//                 RoomEntry newEntry = roomService.joinRoom(roomId, userId);
//                 publisher.publish("ws:room:" + roomId, new ServerEvent.UserJoinedBroadcastEvent(newEntry, event.getSentAt()));
//                 break;
//             }
//             case LEAVE: {
//                 ClientEvent.LeaveEvent leaveEvent = (ClientEvent.LeaveEvent) event;
//                 roomId = leaveEvent.getRoomId();
//                 RoomEntry leftEntry = roomService.leaveRoom(roomId, userId);
//                 if (leftEntry != null) {
//                     publisher.publish("ws:room:" + roomId, new ServerEvent.UserLeftBroadcastEvent(leftEntry, LocalDateTime.now()));
//                 }
//                 break;
//             }
//             case READY: {
//                 ClientEvent.ReadyEvent readyEvent = (ClientEvent.ReadyEvent) event;
//                 roomId = readyEvent.getRoomId();
//                 RoomEntry changedEntry = roomService.setReady(roomId, userId, true);
//                 publisher.publish("ws:room:" + roomId, new ServerEvent.ReadyStatusBroadcastEvent(changedEntry, LocalDateTime.now()));
//                 break;
//             }
//             case NOT_READY: {
//                 ClientEvent.NotReadyEvent notReadyEvent = (ClientEvent.NotReadyEvent) event;
//                 roomId = notReadyEvent.getRoomId();
//                 RoomEntry changedEntry = roomService.setReady(roomId, userId, false);
//                 publisher.publish("ws:room:" + roomId, new ServerEvent.ReadyStatusBroadcastEvent(changedEntry, LocalDateTime.now()));
//                 break;
//             }
//             default:
//                 log.warn("Unsupported event type by RoomPublishEventHandler: {}", event.getType());
//         }
//     }
// }
