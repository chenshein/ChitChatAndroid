package com.example.chitchat.data.Msg;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface MsgDao {
    @Query("SELECT * FROM messages WHERE messageId = :messageId")
    MsgEntity getMessageById(int messageId);
    @Insert
    void createMessage(MsgEntity message);

}
