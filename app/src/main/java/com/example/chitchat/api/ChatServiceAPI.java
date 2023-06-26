package com.example.chitchat.api;

import com.example.chitchat.data.Chat.ChatEntity;
import com.example.chitchat.data.Chat.ChatRespondGet;
import com.example.chitchat.data.Chat.ChatResponse;
import com.example.chitchat.data.Chat.ChatUser;
import com.example.chitchat.data.Msg.Message;
import com.example.chitchat.data.Msg.Message;
import com.example.chitchat.data.User.UserPwsName;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ChatServiceAPI {
    @GET("Chats")
    Call<List<ChatRespondGet>> getChats(@Header("Authorization") String token);

    @POST("Chats")
    Call<ChatResponse> createChat(@Header("Authorization") String token, @Body ChatUser username);

    @DELETE("Chats/{id}")
    Call<Void> deleteChat(@Path("id") int id);

    @POST("Chats/{id}/Messages")
    Call<Void> createMsg(@Header("Authorization") String token,@Path("id") String id, @Body Message msg);

    @GET("Chats/{id}/Messages")
    Call<List<Message>> getMessages(@Path("id") int id);
    @POST("Tokens")
    Call<String> getToken(@Body UserPwsName user);
}
