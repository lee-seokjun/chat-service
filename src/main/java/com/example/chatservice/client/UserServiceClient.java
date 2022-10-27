package com.example.chatservice.client;


import com.example.chatservice.vo.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("user-service")
public interface UserServiceClient {

    @GetMapping("/users/info/{targetId}")
    UserInfo getUserInfo(@PathVariable("targetId") String targetId);
    @GetMapping(value ="/users/info/{targetId}")
    List<UserInfo> getUsersInfos( @PathVariable("targetId") String userIds);

}
