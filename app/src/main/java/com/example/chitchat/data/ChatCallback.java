package com.example.chitchat.data;

import com.example.chitchat.data.Chat.ChatEntity;

import java.util.List;

public interface ChatCallback {
    void onSuccessRes(boolean val);
    void onSuccess(List<ChatEntity> chatEntities);
    void onFailure(String errorMessage);
}