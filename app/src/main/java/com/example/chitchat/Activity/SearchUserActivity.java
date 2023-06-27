package com.example.chitchat.Activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chitchat.R;
import com.example.chitchat.adapter.UsersListAdapterAddChat;
import com.example.chitchat.api.ChatAPI;
import com.example.chitchat.api.UserAPI;
import com.example.chitchat.data.Chat.ChatDao;
import com.example.chitchat.data.Chat.ChatRespondGet;
import com.example.chitchat.data.Chat.ChatsDatabase;
import com.example.chitchat.data.ChatCallback;
import com.example.chitchat.data.ChatForSearchCallback;
import com.example.chitchat.data.GetUserCallback;
import com.example.chitchat.data.LoginCallback;
import com.example.chitchat.data.User.UserDao;
import com.example.chitchat.data.User.UserDatabase;
import com.example.chitchat.data.User.UserEntity;

import java.util.List;

public class SearchUserActivity extends AppCompatActivity {

    EditText searchInput;
    ImageButton searchButton;
    ImageButton backButton;
    RecyclerView recyclerView;
    UsersListAdapterAddChat adapter;

    //get the curr user


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        //get current user
        Bundle extras = getIntent().getExtras();

        searchInput = findViewById(R.id.search_username_input);
        searchButton = findViewById(R.id.search_user_btn);
        backButton = findViewById(R.id.arrow_back_button);
        recyclerView = findViewById(R.id.search_user_recycler_view);

        searchInput.requestFocus();

        //go back to all chats
        backButton.setOnClickListener(v -> {
            onBackPressed();
        });

        adapter = new UsersListAdapterAddChat(this, extras.getString("username"));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Perform the database operation on a background thread
//        Thread thread = new Thread(() -> {
//            List<String> users = performDatabaseQuery();
//            runOnUiThread(() -> setupSearchRecyclerView(users));
//        });
//        thread.start();

        searchButton.setOnClickListener(v -> {
            String searchUser = searchInput.getText().toString();
            String currentUser = extras.getString("username");

            if (searchUser.equals(currentUser)) {
                Toast.makeText(SearchUserActivity.this, "You can't add yourself :)", Toast.LENGTH_SHORT).show();
                return; // Exit the method to prevent further execution
            }

            new Thread(() -> {
                UserDatabase userDatabase = UserDatabase.getUserDatabase(this);
                UserDao userDao = userDatabase.userDao();
                UserEntity.UserWithPws searchUserEntity = userDao.get(searchUser);
                UserEntity.UserWithPws curr_user_entity = userDao.get(currentUser);

                if(searchUserEntity==null){ //no in the local db, but maybe in server db
                    String curr_password = extras.getString("curr_password");
                    new Thread(()->{
                        UserAPI userAPI = new UserAPI();
                        userAPI.login(currentUser, curr_password, new LoginCallback() {
                            @Override
                            public void onLoginSuccess(String token) {
                                //the user exist in server db
                                ChatAPI chatAPI = new ChatAPI();
                                chatAPI.addChatWithTokenForSearch(token, searchUser, new ChatForSearchCallback() {
                                    @Override
                                    public void onGetSuccess(UserEntity user, String id) {
                                        UserEntity new_user = new UserEntity(user.getUsername(),user.getDisplayName(),user.getProfilePic());
                                        chatAPI.delete(token,id);
                                        adapter.setUser(new_user);
                                    }

                                    @Override
                                    public void onGetFailure(String error) {
                                        runOnUiThread(() -> {
                                            Toast.makeText(SearchUserActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                                        });
                                    }
                                });
                            }

                            @Override
                            public void onLoginFailure(String error) {
                                runOnUiThread(() -> {
                                    Toast.makeText(SearchUserActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                                });
                            }
                        });
                    }).start();
                };
                getUserFromAPI(searchUserEntity);
            }).start();
        });
    }


    private void getUserFromAPI(UserEntity.UserWithPws searchUser){
        UserAPI api = new UserAPI();
        api.getUser(searchUser, new GetUserCallback() {
            @Override
            public void onGetSuccess(UserEntity user) {
                adapter.setUser(user);

            }

            @Override
            public void onGetFailure(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(SearchUserActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                });


            }
        });

    }



}



