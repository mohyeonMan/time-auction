// package com.jhpark.time_auction.common.redis.handler.publish;

// import java.util.Set;

// import org.springframework.web.socket.WebSocketSession;

// import com.jhpark.time_auction.common.ws.handler.PublishMessageHandler;
// import com.jhpark.time_auction.common.ws.handler.publish.MessagePublisher;
// import com.jhpark.time_auction.common.ws.model.AbstractMessage;
// import com.jhpark.time_auction.common.ws.model.MessageType;
// import com.jhpark.time_auction.common.ws.model.NoticeMessage;

// public class SessionMessagePublisher implements PublishMessageHandler{

//     private final static Set<MessageType> SUPPORT_TYPES = Set.of(MessageType.NOTICE);
//     private final MessagePublisher<AbstractMessage> messagePublisher;
    
//     private final static String NOTICE_PREFIX = "ws:room:";

//     public SessionMessagePublisher(MessagePublisher<AbstractMessage> messagePublisher) {
//         this.messagePublisher = messagePublisher;
//     }

//     @Override
//     public boolean supports(MessageType messageType) {
//         return SUPPORT_TYPES.contains(messageType);
//     }

//     @Override
//     public void handle(AbstractMessage message) {
//         NoticeMessage noticeMessage = (NoticeMessage) message;
//         messagePublisher.publish(getNoticeDestination(noticeMessage.getRoomId()), noticeMessage);
//     }

//     private String getNoticeDestination(String roomId){
//         return NOTICE_PREFIX + roomId;
//     }
// }
