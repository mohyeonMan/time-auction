package com.jhpark.time_auction.sample;

import java.security.Principal;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SampleController {
    private final SimpMessagingTemplate template;
    private final RedisTemplate<String,Object> redis;

    @MessageMapping("/chat.send") // 클라에서 /app/chat.send 로 전송
    public void handleMessage(HttpSession session, Principal user, @Valid ChatMessage msg) {

        // 1) 같은 인스턴스에 붙은 클라이언트들에게 즉시 전달
        template.convertAndSend("/topic/rooms/" + msg.getRoomId(), msg);

        // 2) Redis Pub/Sub 으로 퍼블리시 → 타 인스턴스가 수신하여 동일 목적지로 송신
        String channel = "chat:rooms:" + msg.getRoomId();
        redis.convertAndSend(channel, msg);
    }

}
