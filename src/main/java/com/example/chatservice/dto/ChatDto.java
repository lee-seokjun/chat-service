package com.example.chatservice.dto;

import com.example.chatservice.jpa.ChatEntity;
import com.example.chatservice.jpa.ChatMember;
import com.example.chatservice.vo.ResponseChatMessage;
import com.example.chatservice.vo.UserInfo;

import lombok.Data;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.io.Serializable;
import java.util.ArrayList;

import java.util.List;

@Data

public class ChatDto implements Serializable {
    private String chatId;
    private String writeId;
    private String writeUser;
    private List<UserInfo> userInfos = new ArrayList<>();
    private List<ResponseChatMessage> chatMessages ;
    private String title;

    public ChatEntity toEntity(){
        ChatEntity chatEntity = new ChatEntity();
        chatEntity.setChatId(chatId);
        chatEntity.setTitle(title);
        chatEntity.setWriteUser(getWriteUser());
        chatEntity.setWriteId(getWriteId());
        userInfos.forEach(
                v ->
                {
                    ChatMember m = new ModelMapper().map(v,ChatMember.class);
                    chatEntity.addMember(m);
                }
        );
        return  chatEntity;
    }
    public ChatEntity toEntity(List<UserInfo> userInfos){
        ChatEntity chatEntity = new ChatEntity();
        chatEntity.setChatId(chatId);
        chatEntity.setTitle(title);
        chatEntity.setWriteUser(getWriteUser());
        chatEntity.setWriteId(getWriteId());
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        userInfos.forEach(
                v ->
                {
                    ChatMember m =mapper.map(v,ChatMember.class);
                    chatEntity.addMember(m);
                }
        );
        return  chatEntity;
    }
    public void addMember(UserInfo userInfo)
    {
        if(!userInfos.contains(userInfo)){

            userInfos.add(userInfo);
        }
    }
    public static ChatDto from(ChatEntity chatEntity)
    {
        ChatDto result = new ChatDto();
        result.setChatId(chatEntity.getChatId());
        result.setTitle(chatEntity.getTitle());
        result.setWriteUser(chatEntity.getWriteUser());
        result.setWriteId(chatEntity.getWriteId());
        List<UserInfo> userInfos = new ArrayList<>();
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        chatEntity.getChatMembers().forEach(
                v->
                {   UserInfo userInfo = mapper.map(v,UserInfo.class);
                    userInfos.add(userInfo);
                }
        );
        result.setUserInfos(userInfos);
//        List<ChatMessageDto> chatMessageDtos = new ArrayList<>();
//        chatEntity.getChatMessages().forEach(
//                v->
//                {
//                    chatMessageDtos.add(ChatMessageDto.from(v));
//                }
//        );
//        result.setChatMessageDtos(chatMessageDtos);
        //result.setChatMemberIds(chatmember);
        return result;
    }

//    public String joinMembers(){
//        return String.join(",",chatMemberIds);
//    }

}
