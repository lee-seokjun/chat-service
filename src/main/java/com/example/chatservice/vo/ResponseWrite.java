package com.example.chatservice.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ResponseWrite {
    private String title;
    private String content;

    private Date createdAt;
    private String writeId;
    private String userId;
}
