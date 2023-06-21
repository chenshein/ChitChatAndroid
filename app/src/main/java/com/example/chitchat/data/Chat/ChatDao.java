package com.example.chitchat.data.Chat;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.chitchat.data.User.UserEntity;

import java.util.List;

@Dao
public interface ChatDao {

    @Query("SELECT * FROM chat")
    List<ChatEntity> getAllChats();
    @Query("SELECT * FROM chat WHERE chatId = :chatId")
    ChatEntity getChatById(int chatId);
    @Query("SELECT * FROM chat WHERE chatId IN " +
            "(SELECT chatId FROM chat WHERE EXISTS " +
            "(SELECT * FROM user WHERE username = :username))")
    List<ChatEntity> getChatsForUser(String username);
    @Insert
    void createChat(ChatEntity chat);
    @Update
    void updateChat(ChatEntity chat);
    @Delete
    void deleteChat(ChatEntity chat);
}
