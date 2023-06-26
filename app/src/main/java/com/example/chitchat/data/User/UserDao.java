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
    @Query("SELECT username FROM userWithPws")
    List<String> getAllUsersName();
    //return User
    @Query("SELECT * FROM userWithPws WHERE username = :username")
    UserEntity.UserWithPws get(String username);

    @Query("SELECT * FROM userWithPws WHERE username = :username and password = :password")
    UserEntity.UserWithPws login(String username,String password);

    @Insert
    void insert(UserEntity.UserWithPws new_user);
    @Transaction
    @Query("SELECT * FROM user WHERE username = :username")
    UserEntity getUserWithFriends(String username);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertFriend(UserEntity friend);




    @Delete
    void delete(UserEntity user);
}
