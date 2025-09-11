package com.jhpark.time_auction.common.ws.config;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Bean
    public DefaultHandshakeHandler httpSessionIdHandshakeHandler() {
        return new DefaultHandshakeHandler() {
            @Override
            protected Principal determineUser(
                    ServerHttpRequest request,
                    WebSocketHandler wsHandler,
                    Map<String, Object> attributes) {

                if (request instanceof ServletServerHttpRequest servlet) {
                    // true: 세션 없으면 생성(안 만들고 싶으면 false로)
                    HttpSession session = servlet.getServletRequest().getSession(true);
                    String id =  session.getId(); // prefix는 선택
                    return () -> id; // Principal#getName()
                }
                // 혹시 Servlet 요청이 아닐 경우 대비
                return () -> "sess:" + UUID.randomUUID().toString();
            }
        };
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트가 연결할 엔드포인트
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .setHandshakeHandler(httpSessionIdHandshakeHandler())
                .withSockJS()
                ;
    }

    @Bean
    public ThreadPoolTaskScheduler stompBrokerTaskScheduler() {
        var ts = new ThreadPoolTaskScheduler();
        ts.setPoolSize(1);
        ts.setThreadNamePrefix("stomp-hb-");
        ts.initialize();
        return ts;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 클라이언트 → 서버로 들어오는 목적지 prefix (Controller의 @MessageMapping 과 매칭)
        registry.setApplicationDestinationPrefixes("/app");

        // 서버 → 클라이언트로 브로드캐스트되는 목적지 prefix
        // 여기서는 심플브로커(메모리)를 켜두되, 노드 간 동기화는 Redis 브리지로 해결
         registry.enableSimpleBroker("/topic", "/queue")
            .setHeartbeatValue(new long[]{10000, 10000})
            .setTaskScheduler(stompBrokerTaskScheduler());

        // 유저 전용 큐 (SimpMessagingTemplate.convertAndSendToUser)
        registry.setUserDestinationPrefix("/user");
    }

    // (선택) 인바운드/아웃바운드 채널 스레드 풀 튜닝
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) { }
    
    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) { }

    public static class NodeId {
        public static final String ID = "node_" + UUID.randomUUID().toString().substring(0, 8);
    }
}