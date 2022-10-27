package com.example.chatservice.dto;

import com.example.chatservice.jpa.ChatMember;
import com.example.chatservice.jpa.ChatMessage;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;


import java.io.Serializable;
import java.util.Date;


@Data
public class ChatMessageDto implements Serializable {

    private String chatId;

    private String message;
    private Date createdAt;
    private String senderName;
    private String senderId;
    public static ChatMessage toEntity(ChatMessageDto dto){
        ChatMessage chatMessage= new ChatMessage();
        //chatMessage.setMessage(dto.message);
        //chatMessage.setChatMember(dto.senderId);
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        return mapper.map(dto,ChatMessage.class);
    }

    public static ChatMessageDto from(ChatMessage entity)
    {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ChatMessageDto chatMessageDto = mapper.map(entity,ChatMessageDto.class);
        chatMessageDto.setChatId(entity.getChat().getChatId());
        return chatMessageDto;
    }


}
