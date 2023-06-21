package com.example.chitchat.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chitchat.R;
import com.example.chitchat.api.ChatAPI;
import com.example.chitchat.data.Chat.ChatDao;
import com.example.chitchat.data.Chat.ChatEntity;
import com.example.chitchat.data.Chat.ChatsDatabase;

import com.example.chitchat.data.User.UserEntity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    String otherUserName;
    String otherUserImg;
    String otherUserDisplayName;

    EditText input_msg;
    ImageButton sent_msg_btn;
    ImageButton back_btn;
    TextView otherDisplayName;
    RecyclerView recyclerView;
    CircleImageView otherImg;



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
        otherImg = findViewById(R.id.other_img);

        //set the display username to be shown
        otherDisplayName.setText(otherUserDisplayName);



        // Set the user's photo to the CircleImageView
        Bitmap userPhotoBitmap = decodeBase64ToBitmap(otherUserImg);
        if (userPhotoBitmap != null) {
            otherImg.setImageBitmap(userPhotoBitmap);
        }
        getOnCreateChatroomModel();

        //go back to chats view
        back_btn.setOnClickListener(v -> {
           Intent intent =  new Intent(this,AllChatsActivity.class);
            intent.putExtra("username",extras.getString("current_username"));

            startActivity(intent);
           finish();
        });

        sent_msg_btn.setOnClickListener(v -> {
            String msg = input_msg.getText().toString().trim();
            if(msg.isEmpty()){
                return;
            }
            sendMessageToUser(msg);
        });

    }

    void sendMessageToUser(String message){

    }

    public Bitmap decodeBase64ToBitmap(String base64String) {
        byte[] decodedBytes = android.util.Base64.decode(base64String, android.util.Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
    public static void getOnCreateChatroomModel() {}

}