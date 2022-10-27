package com.example.chatservice.vo;

import com.example.chatservice.jpa.ChatMember;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class   ResponseChatMessage {
    private String chatId;
    private String senderName;
    private String senderId;
    private String message;
    private Date createdAt;
    private String errorMessage;
}
