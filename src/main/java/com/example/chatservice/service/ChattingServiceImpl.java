package com.example.chatservice.service;

import com.example.chatservice.dto.ChatMessageDto;
import com.example.chatservice.except.NotFoundChatException;
import com.example.chatservice.jpa.*;
import com.example.chatservice.messagequeue.KafkaProducer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.sasl.AuthenticationException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ChattingServiceImpl implements ChattingService{
    private final KafkaProducer kafkaProducer;
    private final ChatRepository chatRepository;
    private final ChatMessageRepository chatMessageRepository;

    public ChattingServiceImpl(KafkaProducer kafkaProducer, ChatRepository chatRepository, ChatMessageRepository chatMessageRepository) {
        this.kafkaProducer = kafkaProducer;
        this.chatRepository = chatRepository;
        this.chatMessageRepository = chatMessageRepository;
    }

    @Override
    @Transactional
    public ChatMessageDto createMessage(ChatMessageDto dto) throws NotFoundChatException, AuthenticationException {
        ChatMessage chatMessage = ChatMessageDto.toEntity(dto);
        ChatEntity chatEntity= chatRepository.findByChatId(dto.getChatId());
        if(chatEntity ==null)
        {
            throw new NotFoundChatException("없음");
        }
        chatMessage.setChat(chatEntity);
        try {
            ChatMember chatmember = chatEntity.findNameByMemberId(dto.getSenderId());
            chatMessage.setSenderId(chatmember.getUserId());
            chatMessage.setSenderName(chatmember.getName());
            ChatMessage result = chatMessageRepository.save(chatMessage);
            kafkaProducer.send("sse-alert-topic",result);
            return ChatMessageDto.from(result);
        }catch(NoSuchElementException e)
        {

            throw new AuthenticationException("채팅방에 속하지 않은 유저가 채팅 시도");
        }catch(Exception e)
        {
            return new ChatMessageDto();
        }

    }


    @Override
    public List<ChatMessageDto> getMessages(String userId, String chatId) {

        List<ChatMessageDto> result = new ArrayList<>();
        ChatEntity chat = chatRepository.findByChatId(chatId);
        chatMessageRepository.findByChat(chat).forEach(
                m->
                {
                    ChatMessageDto res = ChatMessageDto.from(m);
                    result.add(res);
                }
        );

        return result;
    }
}
