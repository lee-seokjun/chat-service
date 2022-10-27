package com.example.chatservice.jpa;



import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name="chat")
public class ChatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name="CHAT_ID")
    private String chatId;

    @Column(nullable=false, name="WRITEID" )
    private String writeId;
    @Column(nullable = false)
    private String writeUser;

    @Column(nullable =false)
    private String title;

    @Column(nullable = false, updatable = false, insertable = false)
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    private Date createAt;

    @OneToMany(mappedBy = "chat")
    private List<ChatMember> chatMembers = new ArrayList<>();

    @OneToMany(mappedBy = "chat")
    private List<ChatMessage> chatMessages = new ArrayList<>();

    @Override
    public String toString(){
        return chatId + " : " +writeUser+"\n \"member\" " +getChatMembers() ;
    }
    public void addMember(ChatMember addMember)
    {
        addMember.setChat(this);
        chatMembers.add(addMember);
    }

    public void deleteMember(ChatMember deleteMember)
    {
        chatMembers.remove(deleteMember);
    }

    public ChatMember findNameByMemberId(String memberId){
        return chatMembers.stream().filter(v->v.getUserId().equals(memberId)).findFirst().get();
    }
//    public void addMessage(ChatMessage chatMessage)
//    {
//        chatMessage.setChat(this);
//        chatMessages.add(chatMessage);
//    }
}
