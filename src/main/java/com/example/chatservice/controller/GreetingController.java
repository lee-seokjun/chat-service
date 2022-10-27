package com.example.chatservice.controller;

import com.example.chatservice.dto.ChatMessageDto;
import com.example.chatservice.service.ChattingService;
import com.example.chatservice.vo.RequestChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GreetingController {

    private final ChattingService chattingService;

    public GreetingController( ChattingService chattingService) {
        this.chattingService = chattingService;
    }

    @MessageMapping("/chat")
    @SendTo
    public ChatMessageDto chat(RequestChatMessage requestChatMessage) throws Exception {
        ChatMessageDto chatMessageDto = new ChatMessageDto();
        chatMessageDto.setSenderId(requestChatMessage.getSenderId());
        chatMessageDto.setMessage(requestChatMessage.getMessage());
        chatMessageDto.setChatId(requestChatMessage.getChatId());
        return chattingService.createMessage(chatMessageDto);
    }
}
