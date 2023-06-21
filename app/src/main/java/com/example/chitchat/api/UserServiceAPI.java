package com.example.chitchat.api;


import com.example.chitchat.data.User.UserEntity;
import com.example.chitchat.data.User.UserPwsName;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserServiceAPI {

 @POST("Tokens")
 Call<String> getToken(@Body UserPwsName user);

 @GET("Users/{username}")
 Call<UserEntity> getUserWithoutPass(@Header("Authorization") String token,@Path("username") String username);

 @POST("Users")
 Call<Void> createUser(@Body UserEntity.UserWithPws newUser);


}