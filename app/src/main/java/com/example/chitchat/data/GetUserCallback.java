package com.example.chitchat.data;

import com.example.chitchat.data.User.UserEntity;

public interface GetUserCallback {
    void onGetSuccess(UserEntity user);
    void onGetFailure(String error);

}
