package com.example.chitchat.api;

import com.example.chitchat.data.Chat.ChatRespondGet;
import com.example.chitchat.data.Chat.ChatResponse;
import com.example.chitchat.data.Chat.ChatUser;
import com.example.chitchat.data.Msg.GetMessagesRespo;
import com.example.chitchat.data.Msg.Message;
import com.example.chitchat.data.Msg.MessageRequest;
import com.example.chitchat.data.User.UserPwsName;

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
    Call<GetMessagesRespo> createMsg(@Header("Authorization") String token, @Path("id") String id, @Body MessageRequest message);

    @GET("Chats/{id}/Messages")
    Call<List<GetMessagesRespo>> getMessages(@Header("Authorization") String token,@Path("id") String id);
    @POST("Tokens")
    Call<String> getToken(@Body UserPwsName user);
}
