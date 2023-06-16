package com.example.chitchat.data;

public interface LoginCallback {
    void onLoginSuccess(String token);
    void onLoginFailure(String error);
}
