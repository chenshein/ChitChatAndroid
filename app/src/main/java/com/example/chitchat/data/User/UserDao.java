package com.example.chitchat.data.User;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<UserEntity> getAllUsers();
    //return User
    @Query("SELECT * FROM user WHERE username = :username")
    UserEntity get(String username);

    @Query("SELECT * FROM user WHERE username = :username and password = :password")
    UserEntity login(String username,String password);

    @Insert
    void insert(UserEntity new_user);
    @Delete
    void delete(UserEntity user);
}
