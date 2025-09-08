package com.jhpark.time_auction.common.ws.model;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public abstract class Message {

    private String body;
    private String senderId;
    private LocalDateTime sentAt;

    public Message(String body, String senderId){
        this.body = body;
        this.sentAt = LocalDateTime.now();
    }

}
