package lk.chat.user;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Message {
    private String senderName;
    private String messageContent;
    private String timestamp;

    public Message(String senderName, String messageContent) {
        this.senderName = senderName;
        this.messageContent = messageContent;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Date date = new Date(timestamp.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        this.timestamp = sdf.format(date);
    }

    public String getSenderName() {
        return senderName;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public String getTimestamp() {
        return timestamp;
    }
}