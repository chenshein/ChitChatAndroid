package com.example.chitchat.data.Msg;

import com.example.chitchat.data.User.UserEntity;
import com.google.gson.annotations.SerializedName;

public class GetMessagesRespo {

    @SerializedName("id")
    private String id;

    @SerializedName("created")
    private String created;

    @SerializedName("content")
    private String content;

    @SerializedName("sender")
    private UserEntity sender;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
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



    public GetMessagesRespo(String id, String created, UserEntity sender, String content) {
        this.id = id;
        this.created = created;
        this.sender = sender;
        this.content = content;
    }


}
