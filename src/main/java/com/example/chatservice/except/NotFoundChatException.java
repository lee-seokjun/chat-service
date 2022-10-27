package com.example.chatservice.except;

public class NotFoundChatException extends Exception{
    public NotFoundChatException(String msg) {
        super(msg);
    }
}
