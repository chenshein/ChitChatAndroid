package com.example.chitchat.data.Chat;

public class ChatResponse {
    private String id;
    private ChatUser user;

    public ChatResponse(String id, ChatUser user) {
        this.id = id;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ChatUser getUser() {
        return user;
    }

    public void setUser(ChatUser user) {
        this.user = user;
    }
}
