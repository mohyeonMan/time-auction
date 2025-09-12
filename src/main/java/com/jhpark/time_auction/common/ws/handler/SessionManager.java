package com.jhpark.time_auction.common.ws.handler;

import org.springframework.web.socket.WebSocketSession;

import com.jhpark.time_auction.common.ws.event.ServerEvent;

import java.util.Collection;

/**
 * 웹소켓 세션을 추가, 제거, 조회하는 역할을 정의하는 인터페이스.
 * <p>
 * 이 인터페이스는 분산 환경에서 세션을 효율적으로 관리하기 위한 계약(Contract)을 명시합니다.
 */
public interface SessionManager {

    /**
     * 새로운 웹소켓 세션을 관리 목록에 추가합니다.
     * * @param session 추가할 웹소켓 세션 객체
     * @return 추가된 세션 객체
     */
    WebSocketSession addSession(WebSocketSession session);

    /**
     * 기존 웹소켓 세션을 관리 목록에서 제거합니다.
     * * @param session 제거할 웹소켓 세션 객체
     * @return 제거된 세션 객체
     */
    WebSocketSession removeSession(WebSocketSession session);

    /**
     * 특정 ID를 가진 웹소켓 세션을 조회합니다.
     * * @param sessionId 조회할 세션의 고유 ID
     * @return 해당 ID의 세션 객체, 존재하지 않으면 null
     */
    WebSocketSession getSession(String sessionId);

    String getSessionKey(String sessionId);

    void sendToSession(String sessionKey, ServerEvent event);
    
    void sendToSession(WebSocketSession session, ServerEvent event);

    /**
     * 현재 관리 중인 모든 웹소켓 세션의 컬렉션을 반환합니다.
     * * @return 모든 웹소켓 세션 객체들의 컬렉션
     */
    Collection<WebSocketSession> getSessions();

    /**
     * 현재 관리 중인 세션의 총 개수를 반환합니다.
     * * @return 세션의 총 개수
     */
    int getSessionCount();

    void renewExpiration(String sessionId);
}