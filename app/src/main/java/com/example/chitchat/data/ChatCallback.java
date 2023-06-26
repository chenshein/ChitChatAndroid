package com.example.chitchat.data;

import com.example.chitchat.data.Chat.ChatEntity;
import com.example.chitchat.data.Chat.ChatRespondGet;

import java.util.List;

public interface ChatCallback {
    void onSuccessRes(String val);
    void onSuccess(List<ChatRespondGet> chatEntities);
    void onFailure(String errorMessage);
}