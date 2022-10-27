package com.example.chatservice.jpa;


import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;


@Data
@Entity
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String senderName;
    private String senderId;


    private String message;

    @ManyToOne
    @JoinColumn(name="CHAT_ID")
    private ChatEntity chat;
    @Column(nullable = false, updatable = false, insertable = false)
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    private String createdAt;
    @Override
    public String toString() {
        return senderId+" : " +message;
    }
}
