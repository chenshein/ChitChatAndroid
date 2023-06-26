package com.example.chitchat.data.Chat;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.chitchat.data.User.UserEntity;


@Database(entities = {ChatEntity.class, UserEntity.class}, version = 4)
public abstract class ChatsDatabase extends RoomDatabase {
    private static final String dbName = "chats";
    private static ChatsDatabase chatsDatabase;

    public static synchronized ChatsDatabase getUserDatabase(Context context) {
        if (chatsDatabase == null) {
            chatsDatabase = Room.databaseBuilder(context, ChatsDatabase.class, dbName).fallbackToDestructiveMigration()
                    .build();
        }
        return chatsDatabase;
    }

    public abstract ChatDao chatDao();

}
