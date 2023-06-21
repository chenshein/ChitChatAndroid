package com.example.chitchat.data.Chat;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.chitchat.Converter.ListConverter;
import com.example.chitchat.Converter.MsgConverter;
import com.example.chitchat.data.Msg.MsgEntity;
import com.example.chitchat.data.User.UserEntity;

import java.util.ArrayList;
import java.util.List;
@Entity(tableName = "chat")
@TypeConverters({ListConverter.class, MsgConverter.class})

public class ChatEntity {
    @PrimaryKey (autoGenerate = true)
    private int chatId;
    private List<UserEntity> users;
    private List<MsgEntity> messages;
    private String lastMessage;



    public ChatEntity() {
        // Empty constructor required by Room
    }
    public ChatEntity(UserEntity user1, UserEntity user2){
        this.users = new ArrayList<>();
        this.users.add(user1);
        this.users.add(user2);
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

    public List<MsgEntity> getMessages() {
        return messages;
    }

    public void setMessages(List<MsgEntity> messages) {
        this.messages = messages;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
