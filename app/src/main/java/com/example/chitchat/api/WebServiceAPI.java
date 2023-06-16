package com.example.chitchat.api;


import com.example.chitchat.data.Chat.ChatEntity;
import com.example.chitchat.data.Msg.MsgEntity;
import com.example.chitchat.data.User.UserEntity;
import com.example.chitchat.data.User.UserPwsName;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WebServiceAPI {
 @GET("Chats")
 Call<List<ChatEntity>> getChats();

 @POST("Chats")
 Call<Void> createChat(@Body ChatEntity newChat);

 @DELETE("Chat/{id}")
 Call<Void> deleteChat(@Path("id") int id);

 @POST("Chat/{id}/Messages")
 Call<Void> createMsg(@Path("id") int id, String msg);

 @GET("Chat/{id}/Messages")
 Call<List<MsgEntity>> getMessages(@Path("id") int id);


 @POST("Tokens")
 Call<String> getToken(@Body UserPwsName user);

 @GET("Users/{username}")
// @Headers("authorization": "Bearer " + jason)
 Call<UserEntity> getUserWithoutPass();

 @POST("Users")
 Call<Void> createUser(@Body UserEntity newUser);


}