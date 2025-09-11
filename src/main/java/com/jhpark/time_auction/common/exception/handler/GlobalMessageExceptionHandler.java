package com.jhpark.time_auction.common.exception.handler;

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.ControllerAdvice;

import com.jhpark.time_auction.common.exception.CustomMessageException;

@ControllerAdvice
public class GlobalMessageExceptionHandler {

    @MessageExceptionHandler
    @SendToUser("/queue/errors") // 오류 메시지를 보낼 목적지
    public String handleGenericException(CustomMessageException ex) {
        return "이벤트 처리중 문제가 발생하였습니다. : " + ex.getMessage();
    }

}