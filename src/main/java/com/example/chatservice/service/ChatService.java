package com.example.chatservice.service;

import com.example.chatservice.dto.ChatDto;
import com.example.chatservice.jpa.ChatEntity;

import java.util.List;

public interface ChatService {
    ChatDto createChat(ChatEntity chatEntity);
    ChatDto createChatByTargetId(ChatEntity chatEntity);

    List<ChatDto> getAllMyChat(String userId);
    ChatDto getChatBychatId(String userId,String ChatId);
    ChatDto createOrGetChat(ChatDto chatDto,String userId);
    ChatDto addMember(String chatId, String userId,String addId);


}
