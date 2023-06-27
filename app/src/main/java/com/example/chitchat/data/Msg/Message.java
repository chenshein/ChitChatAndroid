package com.example.chitchat.data.Msg;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.chitchat.data.User.UserEntity;

@Entity(tableName = "message")
public class Message {
    @PrimaryKey(autoGenerate = true)
    public int messageIdRoom;
    private String msgIdServer;
    private int chatIdRoom;
    private String chatIdServer;

    @Embedded
    public UserEntity sender;
    public String content;
    public String created;

    public Message(String msgIdServer, int chatIdRoom, String chatIdServer, UserEntity sender, String content, String created) {

        this.msgIdServer = msgIdServer;
        this.chatIdRoom = chatIdRoom;
        this.chatIdServer = chatIdServer;
        this.sender = sender;
        this.content = content;
        this.created = created;
    }
    public int getChatIdRoom() {
        return chatIdRoom;
    }

    public void setChatIdRoom(int chatIdRoom) {
        this.chatIdRoom = chatIdRoom;
    }

    public String getChatIdServer() {
        return chatIdServer;
    }

    public void setChatIdServer(String chatIdServer) {
        this.chatIdServer = chatIdServer;
    }



    public int getMessageIdRoom() {
        return messageIdRoom;
    }

    public void setMessageIdRoom(int messageIdRoom) {
        this.messageIdRoom = messageIdRoom;
    }

    public UserEntity getSender() {
        return sender;
    }

    public void setSender(UserEntity sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }



    public int getChatId() {
        return chatIdRoom;
    }

    public void setChatId(int chatId) {
        this.chatIdRoom = chatId;
    }

    public String getMsgIdServer() {
        return msgIdServer;
    }

    public void setMsgIdServer(String msgIdServer) {
        this.msgIdServer = msgIdServer;
    }
}