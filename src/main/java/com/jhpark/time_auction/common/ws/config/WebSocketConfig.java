package com.jhpark.time_auction.common.ws.config;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
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

    @Value("${app.stomp.host}")
    private String relayHost;
    @Value("${app.stomp.port}")
    private Integer relayPort;
    @Value("${app.stomp.vhost}")
    private String vhost;
    @Value("${app.stomp.user}")
    private String user;
    @Value("${app.stomp.pass}")
    private String pass;

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
                    String id = session.getId().substring(0, 8); // prefix는 선택
                    return () -> "session_"+id; // Principal#getName()
                }
                // 혹시 Servlet 요청이 아닐 경우 대비
                return () -> "session_" + UUID.randomUUID().toString().substring(0, 8);
            }
        };
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트가 연결할 엔드포인트
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .setHandshakeHandler(httpSessionIdHandshakeHandler())
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");

        registry.enableStompBrokerRelay("/topic", "/queue")
                .setRelayHost(relayHost)
                .setRelayPort(relayPort)
                .setVirtualHost(vhost)
                .setClientLogin(user)
                .setClientPasscode(pass)
                .setSystemLogin(user)
                .setSystemPasscode(pass)
                .setSystemHeartbeatSendInterval(10_000)
                .setSystemHeartbeatReceiveInterval(10_000)
                .setUserDestinationBroadcast("/topic/unresolved-user-destination")
                .setUserRegistryBroadcast("/topic/simp-user-registry");
    }

    // (선택) 인바운드/아웃바운드 채널 스레드 풀 튜닝
    @Override
    public void configureClientInboundChannel(ChannelRegistration reg) {
        reg.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                var acc = StompHeaderAccessor.wrap(message);
                if (acc.getCommand() == StompCommand.CONNECT && acc.getUser() == null) {
                    // 세션ID를 Principal로 강제(세션 단위 개인 전송 보장)
                    acc.setUser(() -> acc.getSessionId());
                    return MessageBuilder.createMessage(message.getPayload(), acc.getMessageHeaders());
                }
                return message;
            }
        });
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
    }

    public static class NodeId {
        public static final String ID = "node_" + UUID.randomUUID().toString().substring(0, 8);
    }
}