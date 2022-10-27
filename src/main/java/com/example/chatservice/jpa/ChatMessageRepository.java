package com.example.chatservice.jpa;


import org.springframework.data.repository.CrudRepository;

public interface ChatMessageRepository extends CrudRepository<ChatMessage,Long> {
    Iterable<ChatMessage> findByChat(ChatEntity chatId);

}
