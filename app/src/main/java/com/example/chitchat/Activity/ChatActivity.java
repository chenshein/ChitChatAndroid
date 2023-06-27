package com.example.chitchat.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chitchat.R;
import com.example.chitchat.adapter.MessageAdapter;
import com.example.chitchat.api.ChatAPI;
import com.example.chitchat.data.CallBackMessages;
import com.example.chitchat.data.Chat.ChatDao;
import com.example.chitchat.data.Chat.ChatEntity;
import com.example.chitchat.data.Chat.ChatRespondGet;
import com.example.chitchat.data.Chat.ChatsDatabase;
import com.example.chitchat.data.ChatCallback;
import com.example.chitchat.data.Msg.GetMessagesRespo;
import com.example.chitchat.data.Msg.Message;
import com.example.chitchat.data.User.UserDao;
import com.example.chitchat.data.User.UserDatabase;
import com.example.chitchat.data.User.UserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    private RecyclerView recyclerView;

    List<Message> messageList = new ArrayList<>();

    EditText input_msg;
    ImageButton sent_msg_btn;
    ImageButton back_btn;
    TextView otherDisplayName;
    RecyclerView recyclerViewMsg;
    CircleImageView otherImg;
    MessageAdapter messageAdapter;


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
        otherImg = findViewById(R.id.other_img);

        recyclerViewMsg = findViewById(R.id.recycler_msg);

       // messageAdapter= new MessageAdapter(this,messageList,current_user.getUsername());
        recyclerViewMsg.setAdapter(messageAdapter);
        recyclerViewMsg.setLayoutManager(new LinearLayoutManager(this));

        //set the display username to be shown
        otherDisplayName.setText(otherUserDisplayName);

        sent_msg_btn.setOnClickListener(v -> {
            String msg = input_msg.getText().toString().trim();
            if (msg.isEmpty()) {
                return;
            }
            sendMessageToUser(msg);
        });

        // Call the modified findChatId function with the callback
        findChatId(currentUsername, otherUserName, () -> {
            System.out.println("messageList size: " + messageList.size());
            setupRecyclerView();
            // Set the user's photo to the CircleImageView
            Bitmap userPhotoBitmap = decodeBase64ToBitmap(otherUserImg);
            if (userPhotoBitmap != null) {
                runOnUiThread(() -> otherImg.setImageBitmap(userPhotoBitmap));
            }
            // Rest of the code...

            //go back to chats view
            back_btn.setOnClickListener(v -> {
                onBackPressed();
            });


        });
    }

    private void setupRecyclerView() {
        // Create and set the adapter
        MessageAdapter adapter;
        adapter = new MessageAdapter(this, messageList, current_user.getUsername());
        recyclerView = findViewById(R.id.recycler_msg);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getAllMsg(){
        new Thread(()->{
            ChatAPI chatAPI =  new ChatAPI();
            chatAPI.getMessages(chatIdServer, current_user, new CallBackMessages() {
                @Override
                public void onGetSuccess(List<GetMessagesRespo> messagesRespoList) {
                    for (GetMessagesRespo messagesRespo: messagesRespoList){
                        UserEntity sender = messagesRespo.getSender();
                        System.out.println(sender);
                        String content = messagesRespo.getContent();
                        String created = messagesRespo.getCreated();
                        String id = messagesRespo.getId();
                        Message message = new Message(id,chatIdRoom,chatIdServer,sender,content,created);
                        messageList.add(message);
                    }
                }

                @Override
                public void onGetFailure(String error) {

                }
            });
        }).start();
    }

    private void findChatId(String currentUser, String otherUser, final Runnable callback) {
        Executor executor = Executors.newSingleThreadExecutor();
        Runnable asyncRunnable = () -> {
            Context context = this; // Store the context reference

            UserDatabase userDatabase = UserDatabase.getUserDatabase(context);
            UserDao userDao = userDatabase.userDao();
            UserEntity.UserWithPws currentUserEntity = userDao.get(currentUser);
            current_user = currentUserEntity;

            if (currentUserEntity != null) {
                ChatAPI chatAPI = new ChatAPI();

                chatAPI.get(currentUserEntity, new ChatCallback() {
                    @Override
                    public void onSuccessRes(String val) {}

                    @Override
                    public void onSuccess(List<ChatRespondGet> allUserChats) {
                        if (allUserChats == null) {
                            return;
                        }

                        // Find the chatIdServer for the current conversation
                        for (ChatRespondGet chat : allUserChats) {
                            if (chat.getUser().getUsername().equals(otherUser)) {
                                chatIdServer = chat.getId();
                                System.out.println("200 chat id server is " + chatIdServer);
                                break;
                            }
                        }
                        getAllMsg();

                        new Thread(() -> {
                            ChatsDatabase chatsDatabase = ChatsDatabase.getUserDatabase(context);
                            ChatDao chatDao = chatsDatabase.chatDao();
                            List<ChatEntity> allChats = chatDao.getAllChats();

                            // Find the chatIdRoom for the current conversation
                            for (ChatEntity chat : allChats) {
                                System.out.println("chat id server is " + chat.getChatIdServer());
                                if (chat.getChatIdServer().equals(chatIdServer)) {
                                    chatIdRoom = chat.getChatIdRoom();
                                    System.out.println("chat id room is " + chatIdRoom);
                                }
                            }

                            ChatEntity chatEntity = chatDao.getChatById(chatIdRoom);
                            if (chatEntity != null) {
                                messageList = chatEntity.getMessages();
                                System.out.println("messageList size: " + messageList.size());
                            }
                            runOnUiThread(callback);
                        }).start();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        System.out.println("ChatAPI failed: " + errorMessage);
                    }
                });
            }
        };
        executor.execute(asyncRunnable);
    }


    private Bitmap decodeBase64ToBitmap(String base64String) {
        try {
            byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void sendMessageToUser(String message) {
        new Thread(() -> {
            ChatAPI chatAPI = new ChatAPI();
            chatAPI.addMsg(current_user, message, chatIdServer, new CallBackMessages() {
                @Override
                public void onGetSuccess(List<GetMessagesRespo> messagesRespoList) {
                    runOnUiThread(() -> {
                        UserEntity sender = messagesRespoList.get(0).getSender();
                        String content = messagesRespoList.get(0).getContent();
                        String created = messagesRespoList.get(0).getCreated();
                        String id = messagesRespoList.get(0).getId();
                        // Create a new Message instance with the current user and message
                        Message newMessage = new Message(id,chatIdRoom,chatIdServer,sender,content,created);
                        messageList.add(newMessage);


//                        messageAdapter.notifyItemInserted(messageList.size() - 1);
//                        recyclerViewMsg.scrollToPosition(messageList.size() - 1);
//
//                        // Notify the adapter of the data changes
//                        messageAdapter.notifyItemInserted(messageList.size() - 1);

                        // Clear the input message EditText
                        input_msg.setText("");
                        setupRecyclerView();
                    });
                }

                @Override
                public void onGetFailure(String error) {}
            });
        }).start();
    }



    public void updateMessages(Message newMessage) {
        if (Objects.equals(newMessage.getSender(), otherUserName)) {
            messageList.add(newMessage);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    messageAdapter.notifyItemInserted(messageList.size() - 1);
                    recyclerViewMsg.scrollToPosition(messageList.size() - 1);
                }
            });
        } else {
            //do update chats request
        }
    }

}
