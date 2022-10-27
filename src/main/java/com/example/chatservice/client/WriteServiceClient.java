package com.example.chatservice.client;



import com.example.chatservice.vo.ResponseWrite;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("write-service")
public interface WriteServiceClient {

    @GetMapping("/{userId}/write/{writeId}")
    ResponseWrite getWrite(@PathVariable String userId, @PathVariable String writeId) ;
}
