package com.example.chitchat.api;

import com.example.chitchat.MyApplication;
import com.example.chitchat.R;
import com.example.chitchat.data.Chat.ChatEntity;
import com.example.chitchat.data.Chat.ChatUser;
import com.example.chitchat.data.ChatCallback;
import com.example.chitchat.data.User.UserEntity;
import com.example.chitchat.data.User.UserPwsName;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ChatAPI {
    Retrofit retrofit;
    ChatServiceAPI chatServiceAPI;

    public ChatAPI(){
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        chatServiceAPI = retrofit.create(ChatServiceAPI.class);
    }

    //get all chat with the current user
//    public void get(Callback<List<ChatEntity>> callback) {
//        Call<List<ChatEntity>> call = chatServiceAPI.getChats();
//        call.enqueue(callback);
//    }



    //get user chats
    public void get(UserEntity.UserWithPws currentUser, ChatCallback callback) {
        UserPwsName userPwsName = new UserPwsName(currentUser.getUsername(), currentUser.getPassword());
        Call<String> tokenCall = chatServiceAPI.getToken(userPwsName);
        // extract token from response
        tokenCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> tokenCall, Response<String> response) {
                if (response.isSuccessful()) {
                    String token = response.body();
                    String bearerToken = "Bearer " + token;
                    System.out.println("Bearer Token: " + bearerToken + ": " + currentUser.getUsername());
                    Call<List<ChatEntity>> call = chatServiceAPI.getChats(bearerToken);
                    call.enqueue(new Callback<List<ChatEntity>>() {
                        @Override
                        public void onResponse(Call<List<ChatEntity>> call, Response<List<ChatEntity>> response) {
                            if(response.isSuccessful()){
                                List<ChatEntity> chatEntities = response.body();
                                assert response.body() != null;
                                System.out.println(response.body().get(0).toString());
                                callback.onSuccess(chatEntities);
                            } else {
                                String error = "GET failed. Response code: " + response.code() + " Error: " + response.errorBody().toString() + " Message: " + response.message();
                                System.out.println(error);
                                callback.onFailure(error);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<ChatEntity>> call, Throwable t) {
                            String error = "GET creation failed. Error: " + t.getMessage();
                            System.out.println(error);
                            callback.onFailure(error);
                        }
                    });
                } else {
                    String errorBody = response.errorBody().toString();
                    System.out.println("Response unsuccessful. Error body: " + errorBody);
                    callback.onFailure(errorBody);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("Token not created");
                callback.onFailure("Token not created");
            }
        });
    }


    //add chat
    public void addChat(UserEntity.UserWithPws currentUser, String usernameToAdd, ChatCallback callback) {
        UserPwsName userPwsName = new UserPwsName(currentUser.getUsername(), currentUser.getPassword());
        Call<String> tokenCall = chatServiceAPI.getToken(userPwsName);

        tokenCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> tokenCall, Response<String> response) {
                if (response.isSuccessful()) {
                    String token = response.body();
                    String bearerToken = "Bearer " + token;
                    ChatUser chatUser = new ChatUser(usernameToAdd);

                    Call<Void> call = chatServiceAPI.createChat(bearerToken, chatUser);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                System.out.println("Chat created");
                                callback.onSuccessRes(true);
                            } else {
                                String error = "Chat creation failed. Response code: " + response.code() + " Error: " + response.errorBody().toString() + " Message: " + response.message();
                                System.out.println(error);
                                callback.onFailure(error);
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            String error = "Chat creation failed. Error: " + t.getMessage();
                            System.out.println(error);
                            callback.onFailure(error);
                        }
                    });
                } else {
                    String errorBody = response.errorBody().toString();
                    System.out.println("Response unsuccessful. Error body: " + errorBody);
                    callback.onFailure(errorBody);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("Token not created");
                callback.onFailure("Token not created");
            }
        });
    }



    public void getChatById(int chat_id){}


}
