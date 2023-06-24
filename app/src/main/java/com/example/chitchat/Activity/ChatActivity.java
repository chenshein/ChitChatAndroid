package com.example.chitchat.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chitchat.R;
import com.example.chitchat.data.Chat.ChatDao;
import com.example.chitchat.data.Chat.ChatEntity;
import com.example.chitchat.data.Chat.ChatsDatabase;
import com.example.chitchat.data.Msg.Message;
import com.example.chitchat.data.User.UserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    String currentUsername;
    String currentDisplayName;
    String currentUserImg;
    String otherUserName;
    String otherUserImg;
    String otherUserDisplayName;
    int chatId = -1;

    List<Message> messageList = new ArrayList<>();

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
        currentUsername = extras.getString("currentUsername");
        currentDisplayName = extras.getString("currentDisplayName");
//        currentUserImg = extras.getString("currentProfilePic");
        otherUserName = extras.getString("username");
//        otherUserImg = extras.getString("profilePic");
        otherUserDisplayName = extras.getString("displayName");
        Uri profilePicUri = getIntent().getData();
        if (profilePicUri != null) {
            // Save the profile picture URI in otherUserImg
            otherUserImg = profilePicUri.toString();
        }

        sent_msg_btn = findViewById(R.id.chat_send);
        otherDisplayName = findViewById(R.id.other_username);
        input_msg = findViewById(R.id.chat_input_msg);
        back_btn = findViewById(R.id.go_back);
        recyclerView = findViewById(R.id.recycler_msg);
        otherImg = findViewById(R.id.other_img);

        //set the display username to be shown
        otherDisplayName.setText(otherUserDisplayName);


        findChatId(currentUsername, otherUserName);
        System.out.println("chat id is " + chatId);
        new Thread(() -> {
            ChatsDatabase chatsDatabase = ChatsDatabase.getUserDatabase(this);
            ChatDao chatDao = chatsDatabase.chatDao();
            ChatEntity chatEntity = chatDao.getChatById(chatId);
            if (chatEntity != null) {
                messageList = chatEntity.getMessages();
            }
        }).start();

        // Extract the messages from the chat entity


        // Set the user's photo to the CircleImageView
        Bitmap userPhotoBitmap = decodeBase64ToBitmap(otherUserImg);
        if (userPhotoBitmap != null) {
            otherImg.setImageBitmap(userPhotoBitmap);
        }
        getOnCreateChatroomModel();

        //go back to chats view
        back_btn.setOnClickListener(v -> {
            Intent intent = new Intent(this, AllChatsActivity.class);
            intent.putExtra("username", extras.getString("current_username"));
            startActivity(intent);
            finish();
        });

        sent_msg_btn.setOnClickListener(v -> {
            String msg = input_msg.getText().toString().trim();
            if (msg.isEmpty()) {
                return;
            }
            sendMessageToUser(msg, chatId);
        });

    }

    void sendMessageToUser(String message, int chatId) {
        System.out.println("sending message to user: " + message);
        System.out.println("chat id is " + chatId);

        // Add the message to the messageList
        UserEntity currentUser = new UserEntity(currentUsername, currentDisplayName, currentUserImg);

        Message newMessage = new Message(currentUser, message);
        messageList.add(newMessage);

        // Update the messages array for the chat with the given chatId
        addMessageToChat(chatId, newMessage);
        // print messageList items
//        for (Message msg : messageList) {
//            System.out.println(msg.content);
//        }
        input_msg.setText("");
    }

    private void addMessageToChat(int chatId, Message message) {
        new Thread(() -> {
            ChatsDatabase chatsDatabase = ChatsDatabase.getUserDatabase(this);
            ChatDao chatDao = chatsDatabase.chatDao();
            ChatEntity chatEntity = chatDao.getChatById(chatId);
            if (chatEntity != null) {
                System.out.println("chat entity is not null");
                List<Message> chatMessages = chatEntity.getMessages();
                chatMessages.add(message);
                // print chatMessages items
                for (Message msg : chatMessages) {
                    System.out.println(msg.content);
                }
                chatDao.updateChat(chatEntity);
            }
        }).start();
    }

    // TODO: implement this findChatId method
    private void findChatId(String currentUser, String otherUser) {
        final AtomicInteger chatId = new AtomicInteger(-1);

        new Thread(() -> {
            ChatsDatabase chatsDatabase = ChatsDatabase.getUserDatabase(this);
            ChatDao chatDao = chatsDatabase.chatDao();
            List<ChatEntity> allChats = chatDao.getAllChats();

            assert allChats != null;
            for (ChatEntity chatEntity : allChats) {
                List<UserEntity> users = chatEntity.getUsers();

                boolean containsCurrentUser = false;
                boolean containsOtherUser = false;

                for (UserEntity user : users) {
                    if (user.getUsername().equals(currentUser)) {
                        containsCurrentUser = true;
                    } else if (user.getUsername().equals(otherUser)) {
                        containsOtherUser = true;
                    }

                    if (containsCurrentUser && containsOtherUser) {
                        chatId.set(chatEntity.getChatId());
                        this.chatId = chatEntity.getChatId();
                        System.out.println("chat id is " + this.chatId);
                        // set the messageList to be the messages of the chat with the given chatId
                        messageList = chatEntity.getMessages();
                        break;
                    }
                }
//                System.out.println("chat id is " + chatId.get());
            }
        }).start();
    }

    public Bitmap decodeBase64ToBitmap(String base64String) {
        byte[] decodedBytes = android.util.Base64.decode(base64String, android.util.Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static void getOnCreateChatroomModel() {
    }

}