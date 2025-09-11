// package com.jhpark.time_auction.record.handler;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.jhpark.time_auction.common.ws.handler.PublishEventHandler;
// import com.jhpark.time_auction.common.ws.model.in.ClientEvent;
// import com.jhpark.time_auction.common.ws.model.in.ClientEventType;
// import com.jhpark.time_auction.record.service.RecordService;
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
// public class RecordPublishEventHandler implements PublishEventHandler {

//     private final RecordService recordService;
//     private final ObjectMapper objectMapper;

//     @Override
//     public Set<ClientEventType> supports() {
//         return Set.of(ClientEventType.TIME_START, ClientEventType.TIME_END);
//     }

//     @Override
//     public void handle(WebSocketSession session, ClientEvent event) {
//         String userId = session.getAttributes().get("HTTP_SESSION_ID").toString();

//         switch (event.getType()) {
//             case TIME_START:
//                 ClientEvent.TimeStartEvent timeStartEvent = (ClientEvent.TimeStartEvent) event;
//                 recordService.startRecord(userId, timeStartEvent.getRoundId(), LocalDateTime.now());
//                 break;
//             case TIME_END:
//                 ClientEvent.TimeEndEvent timeEndEvent = (ClientEvent.TimeEndEvent) event;
//                 recordService.endRecord(userId, timeEndEvent.getRoundId(), LocalDateTime.now());
//                 break;
//             default:
//                 log.warn("Unsupported event type by RecordPublishEventHandler: {}", event.getType());
//         }
//     }
// }
