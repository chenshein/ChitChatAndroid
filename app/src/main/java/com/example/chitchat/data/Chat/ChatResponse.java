package com.example.chitchat.data.Chat;

import com.example.chitchat.data.User.UserEntity;

public class ChatResponse {
    private String id;
    private UserEntity user;

    public ChatResponse(String id, UserEntity user) {
        this.id = id;
        this.user = user;
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
}
