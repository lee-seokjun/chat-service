package com.example.chatservice.controller;


import com.example.chatservice.dto.ChatDto;

import com.example.chatservice.service.ChatService;

import com.example.chatservice.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/chat")
@Slf4j
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }
    @PostMapping
    public ResponseEntity<ResponseSelectedChat> startChat(@RequestHeader ("userId")  String userId
            , @RequestBody RequestChat requestChat){
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ChatDto  requestDto = mapper.map(requestChat,ChatDto.class);

        /**
         * param (writeId, title)
         * 해당 조건 으로 이미 채팅이 있는지 확인
         */
        ChatDto resultChat = chatService.createOrGetChat(requestDto,userId);


        return ResponseEntity.status(HttpStatus.OK).body(mapper.map(resultChat,ResponseSelectedChat.class));
    }

    @GetMapping
    public ResponseEntity<List<ResponseChat>> getMyChat(@RequestHeader ("userId")  String userId){
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<ChatDto> myChat=chatService.getAllMyChat(userId);
        List<ResponseChat> responseChats = new ArrayList<>();
        myChat.forEach(
                c ->responseChats.add(mapper.map(c,ResponseChat.class))
        );

        return ResponseEntity.status(HttpStatus.OK).body(responseChats);
    }
    @GetMapping("/{chatId}")
    public ResponseEntity<ResponseSelectedChat> getChat(@RequestHeader ("userId")  String userId, @PathVariable String chatId){
        ChatDto chatDto = chatService.getChatBychatId(userId,chatId);
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ResponseSelectedChat responseSelectedChat = mapper.map(chatDto,ResponseSelectedChat.class);
        return ResponseEntity.status(HttpStatus.OK).body(responseSelectedChat);
    }


    @PostMapping("/{chatId}/{addId}")
    public ResponseEntity<ChatDto> addUser(@RequestHeader ("userId")  String userId, @PathVariable String chatId, @PathVariable String addId)
    {
        ChatDto chatDto=chatService.addMember(chatId,userId,addId);
        return ResponseEntity.status(HttpStatus.OK).body(chatDto);
    }


}
