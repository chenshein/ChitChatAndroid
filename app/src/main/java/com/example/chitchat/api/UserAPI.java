package com.example.chitchat.api;

import com.example.chitchat.MyApplication;
import com.example.chitchat.R;
import com.example.chitchat.data.Chat.ChatEntity;
import com.example.chitchat.data.LoginCallback;
import com.example.chitchat.data.User.UserEntity;
import com.example.chitchat.data.User.UserPwsName;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserAPI {
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;

    public UserAPI() {

        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void get() {
        Call<List<ChatEntity>> call = webServiceAPI.getChats();
        call.enqueue(new Callback<List<ChatEntity>>() {
            @Override
            public void onResponse(Call<List<ChatEntity>> call, Response<List<ChatEntity>> response) {
                List<ChatEntity> chats = response.body();
                response.body();
            }

            @Override
            public void onFailure(Call<List<ChatEntity>> call, Throwable t) {
            }
        });
    }

    public void registerUser(UserEntity user) {
        Call<Void> call = webServiceAPI.createUser(user);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    //handle 200 status
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure here
            }
        });
    }

    public void login(String username, String password, LoginCallback callback) {
        UserPwsName userPwsName = new UserPwsName(username, password);
        Call<String> call = webServiceAPI.getToken(userPwsName);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String token = response.body();
                    callback.onLoginSuccess(token);
                } else {
                    String errorBody = response.errorBody().toString();
                    callback.onLoginFailure(errorBody);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.onLoginFailure(t.getMessage());
            }
        });
    }


}