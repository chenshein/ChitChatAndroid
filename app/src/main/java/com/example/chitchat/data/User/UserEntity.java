package com.example.chitchat.data.User;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.chitchat.adapter.ListConverter;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "user")
@TypeConverters(ListConverter.class)
public class UserEntity {
    @PrimaryKey
    @NonNull
    private String username;
    private String password;
    private String displayName;
    private String profilePic;
    private List<UserEntity> userList;


    public UserEntity(String username, String password, String displayName, String profilePic) {
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.profilePic = profilePic;
        this.userList = new ArrayList<>();
    }

    public UserEntity() {
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public List<UserEntity> getUserList() {
        return userList;
    }

    public void setUserList(List<UserEntity> userList) {
        this.userList = userList;
    }

    public void addUserToUserList(UserEntity user) {
        this.userList.add(user);
    }

    public void removeUserFromUserList(UserEntity user) {
        this.userList.remove(user);
    }
}
