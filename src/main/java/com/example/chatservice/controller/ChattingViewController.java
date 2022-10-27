package com.example.chatservice.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChattingViewController {

    @GetMapping("/chattings")
    public String chatPage(){
        return "chattings";
    }


    @GetMapping("/room")
    public String room(){
        return "room";
    }
}
