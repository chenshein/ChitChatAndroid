package com.example.chitchat.data.Chat;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.chitchat.data.Msg.Message;

import java.util.List;

@Dao
public interface ChatDao {

    @Query("SELECT * FROM chat")
    List<ChatEntity> getAllChats();



    @Query("SELECT * FROM chat WHERE chatIdRoom = :chatId")
    ChatEntity getChatById(int chatId);

    @Insert
    void createChat(ChatEntity chat);
    @Update
    void updateChat(ChatEntity chat);
    @Delete
    void deleteChat(ChatEntity chat);

    @Insert
    void insertMessage(Message message);

    @Query("SELECT * FROM message WHERE chatIdRoom = :chatId")
    List<Message> getMessagesByChatId(int chatId);

}
