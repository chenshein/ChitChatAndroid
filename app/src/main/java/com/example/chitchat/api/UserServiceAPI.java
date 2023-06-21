package com.example.chitchat.api;


import com.example.chitchat.data.User.UserEntity;
import com.example.chitchat.data.User.UserPwsName;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserServiceAPI {

 @POST("Tokens")
 Call<String> getToken(@Body UserPwsName user);

 @GET("Users/{username}")
// @Headers("authorization": "Bearer " + jason)
 Call<UserEntity> getUserWithoutPass();

 @POST("Users")
 Call<Void> createUser(@Body UserEntity.UserWithPws newUser);


}