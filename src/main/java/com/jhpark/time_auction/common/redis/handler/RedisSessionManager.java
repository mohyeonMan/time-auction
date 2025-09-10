package com.jhpark.time_auction.common.redis.handler;

import java.time.Duration;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhpark.time_auction.common.redis.model.SessionInfo;
import com.jhpark.time_auction.common.redis.util.RedisTemplateUtil;
import com.jhpark.time_auction.common.ws.config.WebSocketConfig.NodeId;
import com.jhpark.time_auction.common.ws.handler.SessionManager;
import com.jhpark.time_auction.common.ws.model.out.ServerEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisSessionManager implements SessionManager {

    private static final Duration SESSION_TTL = Duration.ofSeconds(30);
    private static final String NS = "ws:session"; // namespace

    private final ConcurrentMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>(); // <sessionId, session>
    private final ConcurrentMap<String, String> sessionKeyToId = new ConcurrentHashMap<>();     // <sessionKey, sessionId>
    private final ConcurrentMap<String, String> sessionIdToKey = new ConcurrentHashMap<>();     // <sessionId, sessionKey>

    private final RedisTemplateUtil redisUtil;
    private final ObjectMapper objectMapper;

    /** 세션 추가 */
    public WebSocketSession addSession(WebSocketSession session) {
        final String sessionId = session.getId();
        final String sessionKey = newRandomSessionKey();
        final String nodeId = NodeId.ID;

        // 1) Redis 저장 (TTL 포함)
        SessionInfo sessionInfo = new SessionInfo(sessionKey, nodeId);
        String redisKey = redisSessionKey(sessionKey); // ws:session:{<sessionKey>}
        redisUtil.set(redisKey, sessionInfo, SESSION_TTL);

        // 2) 로컬 캐시
        sessions.put(sessionId, session);
        sessionKeyToId.put(sessionKey, sessionId);
        sessionIdToKey.put(sessionId, sessionKey);

        log.debug("세션 등록: sessionId={}, sessionKey={}, redisKey={}", sessionId, sessionKey, redisKey);
        return session;
    }

    /** 세션 제거 */
    public WebSocketSession removeSession(WebSocketSession session) {
        final String sessionId = session.getId();
        final String sessionKey = sessionIdToKey.remove(sessionId);

        if (sessionKey != null) {
            sessions.remove(sessionId);
            sessionKeyToId.remove(sessionKey);

            String redisKey = redisSessionKey(sessionKey);
            boolean removed = redisUtil.del(redisKey);
            log.info("세션 제거: sessionId={}, sessionKey={}, redisKey={}, redisRemoved={}",
                    sessionId, sessionKey, redisKey, removed);
        } else {
            log.warn("제거할 세션을 찾을 수 없습니다: sessionId={}", sessionId);
        }
        return session;
    }

    /** 특정 세션키로 개인 메시지 전송 */
    public void sendToSession(String sessionKey, ServerEvent event) {
        String sessionId = sessionKeyToId.get(sessionKey);
        WebSocketSession session = (sessionId == null) ? null : sessions.get(sessionId);
        sendToSession(session, event);
    }

    /** 세션 객체로 직접 전송 */
    public void sendToSession(WebSocketSession session, ServerEvent event) {
        if (session == null || !session.isOpen()) return;
        try {
            String json = objectMapper.writeValueAsString(event);
            session.sendMessage(new TextMessage(json));
        } catch (Exception e) {
            log.warn("세션 전송 실패: sessionId={}, error={}", session.getId(), e.toString());
        }
    }

    @Override
    public WebSocketSession getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    @Override
    public String getSessionKey(String sessionId) {
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

    /** Redis에 저장된 세션 TTL 연장 */
    @Override
    public void renewExpiration(String sessionId) {
        String sessionKey = sessionIdToKey.get(sessionId);
        if (sessionKey == null) return;

        String redisKey = redisSessionKey(sessionKey);
        boolean ok = redisUtil.expire(redisKey, SESSION_TTL);
        if (!ok) {
            log.debug("세션 TTL 연장 실패 또는 키 없음: redisKey={}", redisKey);
        }
    }

    /* ====================== 내부 유틸 ====================== */

    private String newRandomSessionKey() {
        return "session_" + UUID.randomUUID().toString().substring(0, 8);
    }

    /** ws:session:{sessionKey} — 해시태그로 슬롯 고정 */
    private String redisSessionKey(String sessionKey) {
        return redisUtil.key(NS, sessionKey);
    }
}
