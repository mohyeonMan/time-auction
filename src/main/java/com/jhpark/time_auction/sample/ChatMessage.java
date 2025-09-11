package com.jhpark.time_auction.sample;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
    @NotBlank
    private String roomId;
    @NotBlank
    private String senderId;
    @NotBlank
    private String content;

    // getters/setters
}