package com.example.chitchat.data.User;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.chitchat.Converter.ListConverter;

@Entity(tableName = "user")
@TypeConverters(ListConverter.class)
public class UserEntity {
    @NonNull
    @PrimaryKey
    private String username;
    private String displayName;
    private String profilePic;
    private String status;
    private String lastMessage;

    public UserEntity(@NonNull String username, String displayName, String profilePic) {
        this.username = username;
        this.displayName = displayName;
        this.profilePic = profilePic;
        this.status = "offline";
        this.lastMessage = "";
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }


    @Entity(tableName = "userWithPws")
    public static class UserWithPws extends UserEntity{
        String password;
        public UserWithPws(String username, String password, String displayName, String profilePic){
            super(username, displayName, profilePic);
            this.password = password;
        }

        public String getPassword() {return this.password;}
        public void setPassword(String password) {this.password = password;}


    }

}
