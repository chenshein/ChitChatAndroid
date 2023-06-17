package com.example.chitchat.adapter;

import androidx.room.TypeConverter;

import com.example.chitchat.data.User.UserEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ListConverter {
    @TypeConverter
    public static List<UserEntity> fromString(String value) {
        Type listType = new TypeToken<List<UserEntity>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(List<UserEntity> userList) {
        Gson gson = new Gson();
        return gson.toJson(userList);
    }
}