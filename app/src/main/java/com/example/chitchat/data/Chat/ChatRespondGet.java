package com.example.chitchat.data.Chat;

import com.example.chitchat.data.User.UserEntity;

public class ChatRespondGet {

    private String id;
    private UserEntity user;
    private String lastMessage;

    // Constructor
    public ChatRespondGet(String id, UserEntity user, String lastMessage) {
        this.id = id;
        this.user = user;
        this.lastMessage = lastMessage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }


}
