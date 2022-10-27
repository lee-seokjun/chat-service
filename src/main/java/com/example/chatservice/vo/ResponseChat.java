package com.example.chatservice.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)

public class ResponseChat
{
    private String chatId;
    private String writeId;
    private String writeUser;
    private String title;
    private Date createdAt;
    private List<UserInfo> userInfos = new ArrayList<>();
}

