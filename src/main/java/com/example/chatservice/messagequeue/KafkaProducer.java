package com.example.chatservice.messagequeue;

import com.example.chatservice.dto.ChatMessageDto;
import com.example.chatservice.jpa.ChatMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.print.attribute.HashAttributeSet;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class KafkaProducer {
    private KafkaTemplate<String,String> kafkaTemplate ;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void send(String topic, ChatMessage chatMessage){
            String senderId = chatMessage.getSenderId();
            chatMessage.getChat().getChatMembers().forEach(
                    c->{
                        if(!c.getUserId().equals(senderId))
                        {
                            ObjectMapper mapper = new ObjectMapper();
                            Map<String,String> map = new HashMap<>();
                            map.put("fromUser",senderId);
                            map.put("message",chatMessage.getMessage());
                            map.put("toUser",c.getUserId());

                            String jsonInString= "";
                            try {
                                jsonInString = mapper.writeValueAsString(map);
                                kafkaTemplate.send(topic,jsonInString);
                                log.info("Kafka Producer sent data from the Order microservice: " + chatMessage);
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }
                        }




                    }

            );


    }


}
