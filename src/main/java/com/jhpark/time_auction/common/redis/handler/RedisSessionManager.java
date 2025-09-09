package com.jhpark.time_auction.common.redis.handler;

import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhpark.time_auction.common.redis.model.SessionInfo;
import com.jhpark.time_auction.common.ws.config.WebSocketConfig.NodeId;
import com.jhpark.time_auction.common.ws.handler.SessionManager;
import com.jhpark.time_auction.common.ws.model.out.ServerEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RedisSessionManager implements SessionManager {

    private final ConcurrentMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>(); // <sessionId, session>;
    private final ConcurrentMap<String, String> sessionKeyToId = new ConcurrentHashMap<>(); // <sessionKey, sessionId>;
    private final ConcurrentMap<String, String> sessionIdToKey = new ConcurrentHashMap<>(); // <sessionId, sessionKey>;

    private final static String SESSION_PREFIX = "ws:session:";

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisSessionManager(
            RedisTemplate<String, Object> redisTemplate,
            ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * 새로운 세션을 추가합니다.
     * 
     * @param session 추가할 웹소켓 세션
     */

    public WebSocketSession addSession(WebSocketSession session) {
        String sessionId = session.getId();
        String sessionKey = getRandomSessionKey();
        String nodeId = NodeId.ID;

        // 1. Redis에 저장할 SessionInfo 객체 생성
        SessionInfo sessionInfo = new SessionInfo(sessionKey, nodeId);

        // 2. Redis에 저장 (만료 시간 설정 포함)
        String redisSessionKey = getRedisSessionKey(sessionKey);

        // opsForValue()를 사용하여 String-Value 기반의 Redis 명령을 실행
        redisTemplate.opsForValue().set(redisSessionKey, sessionInfo, Duration.ofSeconds(30));

        // 3. 로컬 메모리에 저장
        sessions.put(sessionId, session);
        sessionKeyToId.put(sessionKey, sessionId);
        sessionIdToKey.put(sessionId, sessionKey);

        return session;
    }

    /**
     * 세션을 제거합니다.
     * 
     * @param sessionId 제거할 웹소켓 세션 아이디
     */
    public WebSocketSession removeSession(WebSocketSession session) {
        String sessionId = session.getId();
        String sessionKey = sessionIdToKey.get(sessionId);

        if (sessionKey != null) {
            // 1. 로컬 메모리에서 세션 정보 삭제
            sessions.remove(sessionId);
            sessionKeyToId.remove(sessionKey);
            sessionIdToKey.remove(sessionId);

            // 2. Redis에서 세션 정보 삭제
            String redisKey = getRedisSessionKey(sessionKey);
            redisTemplate.delete(redisKey);

            log.info("세션 제거: " + sessionId + " (Redis Key: " + redisKey + ")");
        } else {
            // 해당 세션 키를 찾을 수 없는 경우
            log.warn("제거할 세션을 찾을 수 없습니다: " + sessionId);
        }

        return session;
    }

    /**
     * 특정 세션에 직접 메시지를 보냅니다.
     * 
     * @param sessionId 메시지를 보낼 세션 ID
     * @param message   보낼 메시지
     */
    public void sendToSession(String sessionKey, ServerEvent event) {
        String sessionId = sessionKeyToId.get(sessionKey);
        WebSocketSession session = sessions.get(sessionId);

        sendToSession(session, event);
    }

    public void sendToSession(WebSocketSession session, ServerEvent event) {
        if (session != null && session.isOpen()) {
            try {
                // 이제 RedisTemplate이 사용하는 ObjectMapper와 동일한 객체를 사용합니다.
                String jsonMessage = objectMapper.writeValueAsString(event);
                session.sendMessage(new TextMessage(jsonMessage));
            } catch (Exception e) {
                // ... (예외 처리)
            }
        }
    }


    private String getRandomSessionKey() {
        return "session_" + UUID.randomUUID().toString().substring(0, 8);
    }

    private String getRedisSessionKey(String sessionKey) {
        return SESSION_PREFIX + sessionKey;
    }

    @Override
    public WebSocketSession getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    @Override
    public String getSessionKey(String sessionId){
        return sessionIdToKey.get(sessionId);
    }

    @Override
    public Collection<WebSocketSession> getSessions() {
        return sessions.values();
    }

    @Override
    public int getSessionCount() {
        return sessions.size();
    }

    /**
     * Redis에 저장된 특정 세션 키의 만료 시간을 연장합니다.
     * @param sessionId 만료 시간을 연장할 웹소켓 세션 ID
     */
    @Override
    public void renewExpiration(String sessionId) {
        String sessionKey = sessionIdToKey.get(sessionId);
        if (sessionKey != null) {
            String redisKey = getRedisSessionKey(sessionKey);
            redisTemplate.expire(redisKey, Duration.ofSeconds(30));
        }
    }

}