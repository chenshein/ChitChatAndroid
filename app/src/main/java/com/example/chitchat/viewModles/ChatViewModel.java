package com.example.chitchat.viewModles;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.chitchat.data.Chat.ChatEntity;
import com.example.chitchat.viewModles.ChatRepository;

import java.util.List;

public class ChatViewModel extends AndroidViewModel {

    private ChatRepository chatRepository;
    private LiveData<List<ChatEntity>> allChats;

    public ChatViewModel(@NonNull Application application) {
        super(application);
        chatRepository = new ChatRepository(application);
        allChats = chatRepository.getAllChats();
    }

    public LiveData<List<ChatEntity>> getAllChats() {
        return allChats;
    }

    public void insertChat(ChatEntity chat) {
        chatRepository.insertChat(chat);
    }

    public void updateChat(ChatEntity chat) {
        chatRepository.updateChat(chat);
    }

    public void deleteChat(ChatEntity chat) {
        chatRepository.deleteChat(chat);
    }
}
