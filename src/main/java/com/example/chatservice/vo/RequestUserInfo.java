package com.example.chatservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RequestUserInfo {
    List<String> userIds;
}
