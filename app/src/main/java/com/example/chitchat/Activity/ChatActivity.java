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
import com.example.chitchat.api.ChatAPI;
import com.example.chitchat.data.Chat.ChatDao;
import com.example.chitchat.data.Chat.ChatEntity;
import com.example.chitchat.data.Chat.ChatRespondGet;
import com.example.chitchat.data.Chat.ChatsDatabase;
import com.example.chitchat.data.ChatCallback;
import com.example.chitchat.data.Msg.Message;
import com.example.chitchat.data.User.UserDao;
import com.example.chitchat.data.User.UserDatabase;
import com.example.chitchat.data.User.UserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    UserEntity.UserWithPws current_user;
    String currentUsername;
    String currentDisplayName;
    String currentUserImg;
    String otherUserName;
    String otherUserImg;
    String otherUserDisplayName;
    String chatIdServer;
    int chatIdRoom;

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
        System.out.println("chat id is " + chatIdServer);
        new Thread(() -> {
            ChatsDatabase chatsDatabase = ChatsDatabase.getUserDatabase(this);
            ChatDao chatDao = chatsDatabase.chatDao();
            List<ChatEntity> allChats = chatDao.getAllChats();
            for (ChatEntity chat : allChats){
                if(chat.getChatIdServer().equals(chatIdServer)){
                    chatIdRoom = chat.getChatIdRoom();
                }
            }
            ChatEntity chatEntity = chatDao.getChatById(chatIdRoom);
            if (chatEntity != null) {
                messageList = chatEntity.getMessages();
            }
        }).start();

        new Thread(() -> {
            UserDatabase userDatabase = UserDatabase.getUserDatabase(this);
            UserDao userDao = userDatabase.userDao();
            current_user= userDao.get(currentUsername);
        }).start();


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
            sendMessageToUser(msg);
        });

    }

    void sendMessageToUser(String message) {

        new Thread(() -> {
            ChatAPI chatAPI = new ChatAPI();
            chatAPI.addMsg(current_user, message, chatIdServer, new ChatCallback() {
                @Override
                public void onSuccessRes(String val) {
                    int i = 0 ;
                    //handle success
                }

                @Override
                public void onSuccess(List<ChatRespondGet> chatEntities) {}

                @Override
                public void onFailure(String errorMessage) {}
            });
        }).start();

        Message newMessage = new Message(current_user, message);
        messageList.add(newMessage);

        // Update the messages array for the chat with the given chatIdServer
        addMessageToChat(newMessage);
        // print messageList items
//        for (Message msg : messageList) {
//            System.out.println(msg.content);
//        }
        input_msg.setText("");
    }

    private void addMessageToChat(Message message) {
        new Thread(() -> {
            ChatsDatabase chatsDatabase = ChatsDatabase.getUserDatabase(this);
            ChatDao chatDao = chatsDatabase.chatDao();
            ChatEntity chatEntity = chatDao.getChatById(chatIdRoom);
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
        Executor executor = Executors.newSingleThreadExecutor();
        Runnable asyncRunnable = () -> {
            UserDatabase userDatabase = UserDatabase.getUserDatabase(this);
            UserDao userDao = userDatabase.userDao();
            UserEntity.UserWithPws currentUserEntity = userDao.get(currentUser);
            // TODO: create an entity that can read the result of the response
            // in order to add a new chat
            if (currentUserEntity != null) {
                ChatAPI chatAPI = new ChatAPI();

                //get all chat with the current user
                chatAPI.get(currentUserEntity, new ChatCallback() {
                    @Override
                    public void onSuccessRes(String val) {}

                    @Override
                    public void onSuccess(List<ChatRespondGet> allUserChats) {
                        //check if the wanted user is in the current user chat list
                        if(allUserChats == null){
                            return;
                        }
                        for (ChatRespondGet chat : allUserChats) {
                            if (chat.getUser().getUsername().equals(otherUser)) {
                                chatIdServer = chat.getId();
                                break;
                            }

                        }
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        System.out.println("chen failed"+errorMessage);
                    }
                });
            }
        };
        executor.execute(asyncRunnable);
    }

    public Bitmap decodeBase64ToBitmap(String base64String) {
        byte[] decodedBytes = android.util.Base64.decode(base64String, android.util.Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static void getOnCreateChatroomModel() {
    }

}