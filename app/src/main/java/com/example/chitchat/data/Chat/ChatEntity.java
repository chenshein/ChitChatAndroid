package com.example.chitchat.data.Chat;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.chitchat.Converter.ListConverter;
import com.example.chitchat.Converter.MsgConverter;
import com.example.chitchat.Converter.TimestampConverter;
import com.example.chitchat.data.Msg.Message;
import com.example.chitchat.data.Msg.Message;
import com.example.chitchat.data.User.UserEntity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
@Entity(tableName = "chat")
@TypeConverters({ListConverter.class, MsgConverter.class, TimestampConverter.class})

public class ChatEntity {
    @PrimaryKey (autoGenerate = true)
    private int chatId;
    private List<UserEntity> users;
    private List<Message> messages;
    private String lastMessage;
    Timestamp lastMsgTimestamp;

    public ChatEntity(List<UserEntity> users, List<Message> messages, String lastMessage, Timestamp lastMsgTimestamp) {
        this.users = users;
        this.messages = messages;
        this.lastMessage = lastMessage;
        this.lastMsgTimestamp = lastMsgTimestamp;
    }


    public Timestamp getLastMsgTimestamp() {
        return lastMsgTimestamp;
    }

    public void setLastMsgTimestamp(Timestamp lastMsgTimestamp) {
        this.lastMsgTimestamp = lastMsgTimestamp;
    }



    public int getChatId() {
        return chatId;
    }

    public void setChatId(@NonNull int chatId) {
        this.chatId = chatId;
    }

    public List<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(List<UserEntity> users) {
        this.users = users;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
