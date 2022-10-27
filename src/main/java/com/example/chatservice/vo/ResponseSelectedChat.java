package com.example.chatservice.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseSelectedChat {
    private String chatId;
    private String writeId;
    private String writeUser;
    private String title;
    private Date createdAt;
    private List<UserInfo> userInfos = new ArrayList<>();
    private List<ResponseChatMessage> chatMessages = new ArrayList<>();
}
