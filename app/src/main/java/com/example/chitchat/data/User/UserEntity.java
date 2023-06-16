package com.example.chitchat.data.User;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "user")
public class UserEntity {
    @PrimaryKey
    @NonNull
    private String username;
    private String password;
    private String displayName;
    private String profilePic;
   // List<UserEntity> user_chats;


    public UserEntity(String username, String password, String displayName,String profilePic) {
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.profilePic = profilePic;
      //  this.user_chats = new ArrayList<>();
    }
    public UserEntity(){}

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

//    public List<UserEntity> getUserFriendsList() {
//        return user_chats;
//    }
//
//    public void addUserToFriends(UserEntity user) {
//        this.user_chats.add(user);
//    }
//
//    public void removeUserFromFriends(UserEntity user) {
//        this.user_chats.remove(user);
//    }
//

}
