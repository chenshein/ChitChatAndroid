package com.example.chitchat.api;

import com.example.chitchat.MyApplication;
import com.example.chitchat.R;
import com.example.chitchat.data.CallBackMessages;
import com.example.chitchat.data.Chat.ChatRespondGet;
import com.example.chitchat.data.Chat.ChatResponse;
import com.example.chitchat.data.Chat.ChatUser;
import com.example.chitchat.data.ChatCallback;
import com.example.chitchat.data.Msg.GetMessagesRespo;
import com.example.chitchat.data.Msg.MessageRequest;
import com.example.chitchat.data.User.UserEntity;
import com.example.chitchat.data.User.UserPwsName;

import java.util.ArrayList;
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
                    Call<List<ChatRespondGet>> call = chatServiceAPI.getChats(bearerToken);
                    call.enqueue(new Callback<List<ChatRespondGet>>() {
                        @Override
                        public void onResponse(Call<List<ChatRespondGet>> call, Response<List<ChatRespondGet>> response) {
                            if(response.isSuccessful()){
                                List<ChatRespondGet> chatEntities = response.body();
                                callback.onSuccess(chatEntities);
                            } else {
                                String error = "GET failed. Response code: " + response.code() + " Error: " + response.errorBody().toString() + " Message: " + response.message();
                                callback.onFailure(error);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<ChatRespondGet>> call, Throwable t) {
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


    public void getMessages(String chatIdServer,UserEntity.UserWithPws current_user, CallBackMessages callback){
        UserPwsName userPwsName = new UserPwsName(current_user.getUsername(), current_user.getPassword());
        Call<String> tokenCall = chatServiceAPI.getToken(userPwsName);

        tokenCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> tokenCall, Response<String> response) {
                String token = response.body();
                String bearerToken = "Bearer " + token;
                Call<List<GetMessagesRespo>> call = chatServiceAPI.getMessages(bearerToken,chatIdServer);
                call.enqueue(new Callback<List<GetMessagesRespo>>() {
                    @Override
                    public void onResponse(Call<List<GetMessagesRespo>> call, Response<List<GetMessagesRespo>> response) {
                        List<GetMessagesRespo> messagesRespoList = response.body();
                        callback.onGetSuccess(messagesRespoList);
                    }

                    @Override
                    public void onFailure(Call<List<GetMessagesRespo>> call, Throwable t) {
                        callback.onGetFailure("failed");
                    }
                });
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

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

                    Call<ChatResponse> call = chatServiceAPI.createChat(bearerToken, chatUser);
                    call.enqueue(new Callback<ChatResponse>() {
                        @Override
                        public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                            if (response.isSuccessful()) {
                                try {
                                    ChatResponse chatResponse = response.body();
                                    String chatId = chatResponse.getId();

                                    // Now you can use the extracted chat ID as needed
                                    // For example, print it to the console
                                    System.out.println("Chat ID: " + chatId);
                                    System.out.println("Chat created");
                                    callback.onSuccessRes(chatId);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                String error = "Chat creation failed. Response code: " + response.code() + " Error: " + response.errorBody().toString() + " Message: " + response.message();
                                System.out.println(error);
                                callback.onFailure(error);
                            }
                        }

                        @Override
                        public void onFailure(Call<ChatResponse> call, Throwable t) {
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






    public void addMsg(UserEntity.UserWithPws currentUser, String msg, String chatId, CallBackMessages callback) {
        UserPwsName userPwsName = new UserPwsName(currentUser.getUsername(), currentUser.getPassword());
        Call<String> tokenCall = chatServiceAPI.getToken(userPwsName);

        tokenCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> tokenCall, Response<String> response) {
                if (response.isSuccessful()) {
                    String token = response.body();
                    String bearerToken = "Bearer " + token;
                    System.out.println("bearerToken: " + bearerToken);

                    MessageRequest messageRequest = new MessageRequest(msg);
                    Call<GetMessagesRespo> call = chatServiceAPI.createMsg(bearerToken, chatId, messageRequest);

                    call.enqueue(new Callback<GetMessagesRespo>() {
                        @Override
                        public void onResponse(Call<GetMessagesRespo> call, Response<GetMessagesRespo> response) {
                            List<GetMessagesRespo> messagesRespo = new ArrayList<>();
                            messagesRespo.add(response.body());
                            callback.onGetSuccess(messagesRespo);
                        }

                        @Override
                        public void onFailure(Call<GetMessagesRespo> call, Throwable t) {
                            String error = "MSG creation failed. Error: " + t.getMessage();
                            System.out.println(error);
                            callback.onGetFailure(error);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("Token not created");
                callback.onGetFailure("Token not created");
            }
        });
    }



    public void getChatById(int chat_id){}


}
