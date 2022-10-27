package com.example.chatservice.controller;

import com.example.chatservice.dto.ChatMessageDto;
import com.example.chatservice.except.NotFoundChatException;
import com.example.chatservice.service.ChatService;
import com.example.chatservice.service.ChattingService;
import com.example.chatservice.vo.RequestChatMessage;
import com.example.chatservice.vo.ResponseChatMessage;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ChattingController {
    private ChattingService chatService;

    public ChattingController(ChattingService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/messages")
    public ResponseEntity<ResponseChatMessage> sendMessage(@RequestHeader("userId")  String userId, @RequestBody RequestChatMessage requestChatMessage ){
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ChatMessageDto chatMessageDto = mapper.map(requestChatMessage,ChatMessageDto.class);
        chatMessageDto.setSenderId(userId);
        try {
            ResponseChatMessage result = mapper.map(chatService.createMessage(chatMessageDto), ResponseChatMessage.class);

            return ResponseEntity.status(HttpStatus.OK).body(result);
        }catch(NotFoundChatException notFoundChatException)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body( ResponseChatMessage.builder().errorMessage("chat Id를 확인하세요.").build());
        }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body( ResponseChatMessage.builder().errorMessage("채팅 멤버가 아닙니다.").build());
        }


    }

    @GetMapping("/messages/{chatId}")
    public ResponseEntity<List<ResponseChatMessage>> getMessage(@RequestHeader ("userId")  String userId, @PathVariable String chatId){
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<ChatMessageDto> result = chatService.getMessages(userId,chatId);

        List<ResponseChatMessage> responseChatMessage = new ArrayList<>();
        result.forEach(res ->responseChatMessage.add(mapper.map(res,ResponseChatMessage.class)));
        return ResponseEntity.status(HttpStatus.OK).body(responseChatMessage);
    }


}
