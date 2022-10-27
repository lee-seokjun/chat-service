package com.example.chatservice.jpa;

import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface ChatRepository extends CrudRepository<ChatEntity,Long> {

        ChatEntity findByChatId(String chatId);
        Iterable<ChatEntity> findByChatMembers(ChatMember chatMember);
        List<ChatEntity> findByWriteId(String writeId);
}
