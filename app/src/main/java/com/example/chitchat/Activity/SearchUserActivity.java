package com.example.chitchat.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.chitchat.adapter.UsersListAdapterAddChat;
import com.example.chitchat.R;
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

        Bundle extras = getIntent().getExtras();

        searchInput = findViewById(R.id.search_username_input);
        searchButton = findViewById(R.id.search_user_btn);
        backButton = findViewById(R.id.arrow_back_button);
        recyclerView = findViewById(R.id.search_user_recycler_view);

        searchInput.requestFocus();

        backButton.setOnClickListener(v -> {
            onBackPressed();
        });

        adapter = new UsersListAdapterAddChat(this,extras.getString("username"));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Perform the database operation on a background thread
        Thread thread = new Thread(() -> {
            List<UserEntity> users = performDatabaseQuery();
            runOnUiThread(() -> setupSearchRecyclerView(users));
        });
        thread.start();


        searchButton.setOnClickListener(v -> {
            String searchTerm = searchInput.getText().toString();
            // TODO: check if the user exists
            if (searchTerm.isEmpty()) {
                searchInput.setError("Invalid Username");
            }
        });
    }

    private List<UserEntity> performDatabaseQuery() {
        UserDatabase userDatabase = UserDatabase.getUserDatabase(this);
        UserDao userDao = userDatabase.userDao();
        return userDao.getAllUsers();
    }

    private void setupSearchRecyclerView(List<UserEntity> userList) {
        adapter.setUsers(userList);
    }
}
