package com.jhpark.time_auction.common.ws.config;

import java.util.UUID;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.jhpark.time_auction.common.ws.handler.SimpleHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new SimpleHandler(), "/ws")
                .setAllowedOrigins("*"); // 테스트용: 모든 도메인 허용
    }

    public static class NodeId {
        public static final String ID = "node_" + UUID.randomUUID().toString().substring(0, 8);
    }
}