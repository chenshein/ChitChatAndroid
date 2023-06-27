package com.example.chitchat.data;

import com.example.chitchat.data.User.UserEntity;

public interface ChatForSearchCallback {

    void onGetSuccess(UserEntity user,String id);
    void onGetFailure(String error);
}
