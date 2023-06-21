package com.example.chitchat.data.Msg;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.chitchat.data.User.UserEntity;

@Entity(tableName = "message")
public class Message {
    @PrimaryKey
    public int messageId;
    @Embedded
    public UserEntity sender;
    public String content;
    public String created;

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
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

    public Message(int messageId, UserEntity sender, String content, String created) {
        this.messageId = messageId;
        this.sender = sender;
        this.content = content;
        this.created = created;

    }
}