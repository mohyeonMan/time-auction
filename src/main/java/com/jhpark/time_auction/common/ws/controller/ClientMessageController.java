// package com.jhpark.time_auction.common.ws.controller;

// import com.jhpark.time_auction.common.ws.model.in.ClientEvent;
// import com.jhpark.time_auction.room.service.RoomService; // Assuming RoomService is needed for JOIN/CHAT
// import com.jhpark.time_auction.common.ws.handler.SessionManager; // Assuming SessionManager is needed

// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.messaging.handler.annotation.MessageMapping;
// import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
// import org.springframework.stereotype.Controller;

// @Slf4j
// @Controller
// @RequiredArgsConstructor
// public class ClientMessageController {

//     private final RoomService roomService; // Placeholder
//     private final SessionManager sessionManager; // Placeholder

//     // Handles messages sent to /app/join
//     @MessageMapping("/join")
//     public void handleJoinEvent(ClientEvent.JoinEvent message, SimpMessageHeaderAccessor headerAccessor) {
//         log.info("Received JOIN event: {}", message);
//         // Access session ID: headerAccessor.getSessionId()
//         // Access user principal: headerAccessor.getUser()
//         // Re-implement join logic here, using roomService and sessionManager
//         // Example: roomService.joinRoom(message.getRoomId(), headerAccessor.getSessionId());
//     }

//     // Handles messages sent to /app/chat
//     @MessageMapping("/chat")
//     public void handleChatEvent(ClientEvent.ChatEvent message, SimpMessageHeaderAccessor headerAccessor) {
//         log.info("Received CHAT event: {}", message);
//         // Re-implement chat logic here, using roomService
//         // Example: roomService.sendMessage(message.getRoomId(), message.getMessage(), headerAccessor.getSessionId());
//     }

//     // You will add more @MessageMapping methods for other ClientEvent types
//     // For example:
//     // @MessageMapping("/ready")
//     // public void handleReadyEvent(ClientEvent.ReadyEvent message, SimpMessageHeaderAccessor headerAccessor) {
//     //     log.info("Received READY event: {}", message);
//     // }
// }