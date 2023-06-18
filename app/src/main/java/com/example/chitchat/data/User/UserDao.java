package com.example.chitchat.data.User;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    @Update
    void updateUser(UserEntity user);
    @Query("SELECT * FROM user")
    List<UserEntity> getAllUsers();
    //return User
    @Query("SELECT * FROM user WHERE username = :username")
    UserEntity get(String username);

    @Query("SELECT * FROM user WHERE username = :username and password = :password")
    UserEntity login(String username,String password);

    @Insert
    void insert(UserEntity new_user);
    @Transaction
    @Query("SELECT * FROM user WHERE username = :username")
    UserEntity getUserWithFriends(String username);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertFriend(UserEntity friend);


    @Delete
    void delete(UserEntity user);
}
