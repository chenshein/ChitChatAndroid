package com.example.chitchat.data.Chat;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.chitchat.data.Msg.MsgEntity;
import com.example.chitchat.data.User.UserEntity;

import java.util.List;
@Entity(tableName = "chat")
public class ChatEntity {
    @PrimaryKey
    @NonNull
    private int chatId;
    private List<UserEntity> participants;
    private List<MsgEntity> messages;

    public int getChatId() {
        return chatId;
    }

    public void setChatId(@NonNull int chatId) {
        this.chatId = chatId;
    }

    public List<UserEntity> getParticipants() {
        return participants;
    }

    public void setParticipants(List<UserEntity> participants) {
        this.participants = participants;
    }

    public List<MsgEntity> getMessages() {
        return messages;
    }

    public void setMessages(List<MsgEntity> messages) {
        this.messages = messages;
    }

}
