package com.example.chatservice.service;

import com.example.chatservice.dto.ChatMessageDto;
import com.example.chatservice.except.NotFoundChatException;

import javax.security.sasl.AuthenticationException;
import java.util.List;

public interface ChattingService {
    List<ChatMessageDto> getMessages(String userId, String chatId);

    ChatMessageDto createMessage(ChatMessageDto chatMessage) throws NotFoundChatException, AuthenticationException;
}
