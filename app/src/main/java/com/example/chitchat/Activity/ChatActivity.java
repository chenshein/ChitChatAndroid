package com.example.chitchat.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.chitchat.R;
import com.example.chitchat.data.User.UserEntity;

public class ChatActivity extends AppCompatActivity {

    String otherUserName;
    String otherUserImg;
    String otherUserDisplayName;


    EditText input_msg;
    ImageButton sent_msg_btn;
    ImageButton back_btn;
    TextView otherDisplayName;
    RecyclerView recyclerView;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Bundle extras = getIntent().getExtras();

        otherUserName = extras.getString("username");
        otherUserImg = extras.getString("profilePic");
        otherUserDisplayName = extras.getString("displayName");

        sent_msg_btn =findViewById(R.id.chat_send);
        otherDisplayName = findViewById(R.id.other_username);
        input_msg = findViewById(R.id.chat_input_msg);
        back_btn = findViewById(R.id.go_back);
        recyclerView = findViewById(R.id.recycler_msg);
        //go back to chats view
        back_btn.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChatActivity.class);
            startActivity(intent);
        });
        otherDisplayName.setText(otherUserDisplayName);

        getOnCreateChatroomModel();


    }

    public void getOnCreateChatroomModel() {

    }

}