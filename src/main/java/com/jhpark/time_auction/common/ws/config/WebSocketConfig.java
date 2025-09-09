package com.jhpark.time_auction.common.ws.config;

import java.util.UUID;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketHandler webSocketHandler;

    public WebSocketConfig(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 주입받은 WebSocketHandler 빈을 등록합니다.
        registry.addHandler(webSocketHandler, "/ws")
                .setAllowedOriginPatterns("*"); // CORS 설정
}
    public static class NodeId {
        public static final String ID = "node_" + UUID.randomUUID().toString().substring(0, 8);
    }
}