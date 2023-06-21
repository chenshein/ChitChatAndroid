package com.example.chitchat.data.User;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.chitchat.Converter.ListConverter;

@Entity(tableName = "user")
@TypeConverters(ListConverter.class)
public class UserEntity {
    @PrimaryKey
    @NonNull
    private String username;
    private String displayName;
    private String profilePic;


    public UserEntity(String username, String displayName, String profilePic) {
        this.username = username;
        this.displayName = displayName;
        this.profilePic = profilePic;
    }

    public UserEntity() {}

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
