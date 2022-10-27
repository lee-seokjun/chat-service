package com.example.chatservice.vo;

import lombok.Data;

@Data
public class RequestChatMessage {
    private String chatId;
    private String message;
    private String senderId;
}
