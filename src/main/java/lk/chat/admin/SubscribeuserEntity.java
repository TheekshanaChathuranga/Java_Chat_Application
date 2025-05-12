package lk.chat.admin;


import jakarta.persistence.*;

@Entity
@Table(name = "subscribeuser", schema = "javachat")
public class SubscribeuserEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "chat_id")
    private int chatId;
    @Basic
    @Column(name = "user_id")
    private int userId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubscribeuserEntity that = (SubscribeuserEntity) o;

        if (id != that.id) return false;
        if (chatId != that.chatId) return false;
        if (userId != that.userId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + chatId;
        result = 31 * result + userId;
        return result;
    }

    @Override
    public String toString() {
        return "SubscribeuserEntity{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", userId=" + userId +
                '}';
    }
}
