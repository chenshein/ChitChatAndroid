package com.example.chitchat.data;

import com.example.chitchat.data.Msg.GetMessagesRespo;
import com.example.chitchat.data.User.UserEntity;

import java.util.List;

public interface CallBackMessages {

    void onGetSuccess(List<GetMessagesRespo> messagesRespoList);
    void onGetFailure(String error);
}
