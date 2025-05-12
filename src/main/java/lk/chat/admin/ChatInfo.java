package lk.chat.admin;


import jakarta.persistence.*;

@Entity
@Table(name = "chatinfo")
public class ChatInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int chat_id;
    @Column(name = "chat_name")
    private String chatName;
    @Column(name = "chat_discription")
    private String chatDescription;

    public ChatInfo() {
    }

    public ChatInfo(String chatName, String chatDescription) {
        this.chatName = chatName;
        this.chatDescription = chatDescription;
    }

    public int getChat_id() {
        return chat_id;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getChatDescription() {
        return chatDescription;
    }

    public void setChatDescription(String chatDescription) {
        this.chatDescription = chatDescription;
    }


}
