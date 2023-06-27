package com.example.chitchat.data.Msg;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface MsgDao {
    @Query("SELECT * FROM message WHERE messageIdRoom = :messageId")
    Message getMessageById(int messageId);
    @Insert
    void createMessage(Message message);

}
