package com.example.chatservice.service;

import com.example.chatservice.client.UserServiceClient;
import com.example.chatservice.client.WriteServiceClient;
import com.example.chatservice.dto.ChatDto;
import com.example.chatservice.jpa.*;
import com.example.chatservice.vo.ResponseChatMessage;
import com.example.chatservice.vo.UserInfo;
import com.example.chatservice.vo.ResponseWrite;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.*;

@Service
@Slf4j
public class ChatServiceImpl implements ChatService{
    ChatRepository chatRepository;
    ChatMessageRepository chatMessageRepository;
    ChatMemberRepository chatMemberRepository;
    WriteServiceClient writeServiceClient;
    UserServiceClient userServiceClient;
    CircuitBreakerFactory circuitBreakerFactory;

    public ChatServiceImpl(ChatRepository chatRepository, ChatMemberRepository chatMemberRepository,
                           ChatMessageRepository chatMessageRepository, WriteServiceClient writeServiceClient,
                           CircuitBreakerFactory circuitBreakerFactory,UserServiceClient userServiceClient) {
        this.chatRepository = chatRepository;
        this.chatMemberRepository = chatMemberRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.writeServiceClient = writeServiceClient;
        this.circuitBreakerFactory = circuitBreakerFactory;
        this.userServiceClient=userServiceClient;


    }
    public ChatEntity isExistingChat(ChatDto chatDto,String userId){
        List<ChatEntity> test = chatRepository.findByWriteId(chatDto.getWriteId());
        return test.stream().filter(v->v.getChatMembers().stream().filter(f -> f.getUserId().equals(userId)).findFirst().get()!=null).findFirst().orElseGet(null);

//                .forEach(v->
//        {
//            ChatMember chatMember= v.getChatMembers().stream().filter(f -> f.getUserId().equals(userId)).findFirst().get();
//            if(chatMember!=null)
//            {
//                return v;
//            }
//        });

    }
    @Override
    public ChatDto createOrGetChat(ChatDto chatDto,String userId)  {
        ChatEntity chat =isExistingChat(chatDto,userId);
        if(chat==null) {
            String chatId = UUID.randomUUID().toString();
            chatDto.setChatId(chatId);
        }
        else{
            return getChatBychatId(userId,chatDto.getChatId());
        }


        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        ResponseWrite write = circuitBreaker.run(() ->
                        writeServiceClient.getWrite(userId,chatDto.getWriteId())
                ,throwable -> null);
        if(write==null)
        {
            log.error("없는 글입니다.");
            return new ChatDto();
        }
        String writeId = write.getUserId();
        chatDto.setWriteUser(writeId);
        if(StringUtils.isEmpty(chatDto.getTitle())) {
            chatDto.setTitle(write.getTitle());
        }
        //글쓴이 추가

        List<String> requestUserInfo = Arrays.asList(userId,write.getUserId());
        List<UserInfo> userInfo = circuitBreaker.run(
                () -> userServiceClient.getUsersInfos(String.join(",",requestUserInfo))
                ,throwable -> new ArrayList<>()
        );
        //Write Service 에서 처리 해줘야지...
        if(!userInfo.contains(new UserInfo(writeId)))
        {
            log.error("글쓴이가 존재하지 않음");
            //return new ChatDto();
        }
        //gateway에서 없는 유저 걸러야지
        if(!userInfo.contains(new UserInfo(userId)))
        {
            log.error("로그인 유저가 존재하지 않음");
            return new ChatDto();
        }
        userInfo.forEach(chatDto::addMember);

        ChatEntity chatEntity = chatDto.toEntity(userInfo);
        return createChat( chatEntity);
    }

    @Override
    @Transactional
    public ChatDto createChat(ChatEntity chatEntity) {

        ChatEntity createdChat = chatRepository.save(chatEntity);
        chatEntity.getChatMembers().forEach(
                v ->
                {
                    try {
                        chatMemberRepository.save(v);
                    }catch (Exception e)
                    {
                        log.error(e.getMessage());
                    }
                }
        );


        ChatDto resultDto = ChatDto.from(createdChat);

        log.info(String.valueOf(resultDto));
        return resultDto;
    }


    @Override
    public ChatDto getChatBychatId(String userId,String chatId){
        ChatEntity chatEntity = chatRepository.findByChatId(chatId);
        ChatDto chatDto = ChatDto.from(chatEntity);
        List<ResponseChatMessage> responseChatMessage = new ArrayList<>();
        ModelMapper mapper= new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        chatEntity.getChatMessages().forEach(res ->responseChatMessage.add(mapper.map(res,ResponseChatMessage.class)));
        chatDto.setChatMessages(responseChatMessage);
        return chatDto;
    }

    @Override
    @Transactional
    public List<ChatDto> getAllMyChat(String userId) {

        Iterable<ChatMember> chatMembers= chatMemberRepository.findByUserId(userId);

        List<ChatDto> resultList = new ArrayList<>();
        chatMembers.forEach(
                v->
                {
                    ChatDto c = ChatDto.from(v.getChat());
                    resultList.add(c);

                }
        );
        return resultList;

    }
    @Override
    @Transactional
    public ChatDto addMember(String chatId, String userId,String addId) {
        ChatEntity entity =chatRepository.findByChatId(chatId);
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");
        UserInfo addMember =          circuitBreaker.run(
                () -> userServiceClient.getUserInfo(addId)
                ,throwable -> null
        );
        ChatDto chatDto = ChatDto.from(entity);
        addMember.setUserId(addId);
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ChatMember chatMember = mapper.map(addMember,ChatMember.class);
        entity.addMember(chatMember);
        chatMemberRepository.save(chatMember);
        return ChatDto.from(entity);
    }




    public String getDefaultString(String str , String defalut){
        try {
            if (str != null && !str.equals("")) {
                    return str;
            }
        }catch (Exception e){
            log.error("value is null");
            return defalut;
        }
        return defalut;
    }

    @Override
    public ChatDto createChatByTargetId(ChatEntity chatEntity) {
        return null;
    }
}
