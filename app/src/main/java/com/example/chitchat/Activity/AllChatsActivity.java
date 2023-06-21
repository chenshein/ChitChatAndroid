package com.example.chitchat.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chitchat.R;
import com.example.chitchat.data.User.UserDao;
import com.example.chitchat.data.User.UserDatabase;
import com.example.chitchat.data.User.UserEntity;
import com.example.chitchat.fragment.ChatFragment;
import com.example.chitchat.fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AllChatsActivity extends AppCompatActivity {

    ImageButton addButton;
    ChatFragment chatFragment;
    ProfileFragment profileFragment;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chats_screen);

        //get the curr user
        Bundle extras = getIntent().getExtras();

        chatFragment = new ChatFragment(extras.getString("username"));
        profileFragment = new ProfileFragment();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        addButton = findViewById(R.id.ic_add_user);

        addButton.setOnClickListener((v) -> {
            Intent intent = new Intent(this, SearchUserActivity.class);
            intent.putExtra("username", extras.getString("username")); //pass the username
            startActivity(intent);
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menu_chats) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, chatFragment).commit();
                }
                if (item.getItemId() == R.id.menu_profile) {
                    Bundle profileBundle = new Bundle();
                    profileBundle.putString("username", extras.getString("username"));
                    profileFragment.setArguments(profileBundle);

                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, profileFragment).commit();
                }
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.menu_chats);

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshUserList();
    }

    public void refreshUserList() {
        if (chatFragment != null) {
            chatFragment.refreshUserList();
        }
    }
}
