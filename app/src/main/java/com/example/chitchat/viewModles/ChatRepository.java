package com.example.chitchat.viewModles;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.chitchat.data.Chat.ChatDao;
import com.example.chitchat.data.Chat.ChatEntity;
import com.example.chitchat.data.Chat.ChatsDatabase;
import com.example.chitchat.data.User.UserEntity;

import java.util.List;

public class ChatRepository {

    private ChatDao chatDao;
    private LiveData<List<ChatEntity>> allChats;

    public ChatRepository(Application application) {
        ChatsDatabase database = ChatsDatabase.getUserDatabase(application);
        chatDao = database.chatDao();
        allChats = (LiveData<List<ChatEntity>>) chatDao.getAllChats();
    }

    public LiveData<List<ChatEntity>> getAllChats() {
        return allChats;
    }

    public void insertChat(ChatEntity chat) {
        chatDao.createChat(chat);
    }

    public void updateChat(ChatEntity chat) {
        chatDao.updateChat(chat);
    }

    public void deleteChat(ChatEntity chat) {
        chatDao.deleteChat(chat);
    }
}
