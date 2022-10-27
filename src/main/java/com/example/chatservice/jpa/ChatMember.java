package com.example.chatservice.jpa;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="CHAT_MEMBER"
    ,uniqueConstraints = {
        @UniqueConstraint(
                name= "UNIQUE",
                columnNames={"MEMBER_ID", "CHAT_ID"}
        )
})

public class ChatMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name="MEMBER_ID")
    private String userId;
    @Column(name="MEMBER_NAME")
    private String name;
    @Column(name="MEMBER_EMAIL")
    private String email;
    @ManyToOne
    @JoinColumn(name="CHAT_ID")
    private ChatEntity chat;



    @Override
    public String toString(){
        return userId;
    }
}
