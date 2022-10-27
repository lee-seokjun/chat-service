package com.example.chatservice.controller;

import com.example.chatservice.dto.ChatDto;
import com.example.chatservice.vo.ResponseSelectedChat;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DirectChatController {

    @PostMapping("/chat/{targetId}")
    public ResponseEntity<ResponseSelectedChat> startChat(@RequestHeader("userId")  String userId
            , @PathVariable String targetId){
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ChatDto resultChat = new ChatDto();
        return ResponseEntity.status(HttpStatus.OK).body(mapper.map(resultChat,ResponseSelectedChat.class));
    }
}
