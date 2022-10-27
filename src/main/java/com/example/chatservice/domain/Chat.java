package com.example.chatservice.domain;

import lombok.Data;

import java.util.List;

@Data
public class Chat {
    private String chatId;
    private String writeId;
    private String masterMember;
    private List<String> members;


}
