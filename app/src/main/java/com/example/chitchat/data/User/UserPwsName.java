package com.example.chitchat.data.User;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

public class UserPwsName {
    @PrimaryKey
    @NonNull
    private String username;
    private String password;

    public UserPwsName(@NonNull String username, String password) {
        this.username = username;
        this.password = password;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
