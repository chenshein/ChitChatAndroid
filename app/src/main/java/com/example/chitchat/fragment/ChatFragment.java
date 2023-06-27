package com.example.chitchat.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chitchat.Activity.ChatActivity;
import com.example.chitchat.R;
import com.example.chitchat.adapter.ChatAdapter;
import com.example.chitchat.api.ChatAPI;
import com.example.chitchat.data.CallBackMessages;
import com.example.chitchat.data.Chat.ChatDao;
import com.example.chitchat.data.Chat.ChatEntity;
import com.example.chitchat.data.Chat.ChatItemData;
import com.example.chitchat.data.Chat.ChatsDatabase;
import com.example.chitchat.data.Msg.GetMessagesRespo;
import com.example.chitchat.data.User.UserDao;
import com.example.chitchat.data.User.UserDatabase;
import com.example.chitchat.data.User.UserEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ChatFragment extends Fragment implements ChatAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private UserEntity.UserWithPws current_user;
    private String currentUser_str = "";
    private ChatAdapter adapter;
    List<ChatEntity> chats;

    public ChatFragment(String current_username) {
        this.currentUser_str = current_username;
        new Thread(() -> {
            UserDatabase userDatabase = UserDatabase.getUserDatabase(getContext());
            UserDao userDao = userDatabase.userDao();
            current_user = userDao.get(current_username);
        }).start();
    }


    public ChatFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the fragment layout
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        // Initialize the recyclerView
        recyclerView = rootView.findViewById(R.id.recyclerView_chats);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new Thread(() -> {
            ChatsDatabase chatsDatabase = ChatsDatabase.getUserDatabase(getContext());
            ChatDao chatDao = chatsDatabase.chatDao();
            //all chats in ROOM db
            chats = chatDao.getAllChats();
            List<ChatEntity> user_chats = new ArrayList<>();

            //in order to get all user's chats
            for (ChatEntity chat : chats) {
                List<UserEntity> users = chat.getUsers();
                for (UserEntity user : users) {
                    if (user.getUsername().equals(currentUser_str)) {
                        user_chats.add(chat);
                    }
                }
            }
            chats = user_chats;

            // Call setupRecyclerView() on the main thread
            requireActivity().runOnUiThread(() -> setupRecyclerView(chats));
        }).start();
    }

    private void setupRecyclerView(List<ChatEntity> chats) {
        createChatItems(chats)
                .thenAccept(chatItems -> {
                    // Create and set the adapter
                    adapter = new ChatAdapter(getContext(), chatItems);
                    adapter.setOnItemClickListener(this); // Set the item click listener
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                })
                .exceptionally(ex -> {
                    // Handle any exceptions that occurred during chat item creation
                    return null;
                });
    }

    private CompletableFuture<List<ChatItemData>> createChatItems(List<ChatEntity> all_chats) {
        List<CompletableFuture<ChatItemData>> chatItemFutures = new ArrayList<>();

        for (ChatEntity chat : all_chats) {
            for (UserEntity user : chat.getUsers()) {
                if (!user.getUsername().equals(currentUser_str)) {
                    UserEntity otherUser = user;

                    CompletableFuture<ChatItemData> chatItemFuture = new CompletableFuture<>();
                    chatItemFutures.add(chatItemFuture);

                    getMsgApi(chat, (content, time) -> {
                        chatItemFuture.complete(new ChatItemData(
                                otherUser.getUsername(),
                                otherUser.getProfilePic(),
                                otherUser.getDisplayName(),
                                content, time));
                    });
                }
            }
        }

        return CompletableFuture.allOf(chatItemFutures.toArray(new CompletableFuture[0]))
                .thenApply(v -> chatItemFutures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()));
    }

    // Implement the item click listener method
    //TODO: change to chat screen with the other user on click
    @Override
    public void onItemClick(ChatItemData chatItem) {
        Intent intent = new Intent(getContext(), ChatActivity.class);
        // TODO: get username from chatItem
        intent.putExtra("username", chatItem.getUsername());
        intent.putExtra("displayName", chatItem.getDisplayName());
        intent.putExtra("currentUsername", current_user.getUsername());
        intent.putExtra("currentDisplayName", current_user.getDisplayName());

        // Set the profile picture URI
        Uri profilePicUri = Uri.parse(chatItem.getProfilePic());
        intent.setData(profilePicUri);

        // Set the flag to grant read URI permission
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(intent);
    }


    public void getMsgApi(ChatEntity chat, MessageCallback callback) {
        new Thread(() -> {
            ChatAPI chatAPI = new ChatAPI();
            chatAPI.getMessages(chat.getChatIdServer(), current_user, new CallBackMessages() {
                @Override
                public void onGetSuccess(List<GetMessagesRespo> messagesRespoList) {
                    String content, time;
                    if (messagesRespoList.isEmpty()) {
                        content = "";
                        time = "";
                    } else {
                        GetMessagesRespo msg = messagesRespoList.get(0);
                        content = msg.getContent();
                        time =formatDateTime(msg.getCreated());


                    }
                    callback.onMessageReceived(content, time);
                }

                @Override
                public void onGetFailure(String error) {
                    // Handle the failure case
                }
            });
        }).start();
    }


    public String formatDateTime(String dateString) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm | MM/dd", Locale.US);

        try {
            Date date = inputFormat.parse(dateString);
            if (date != null) {
                return outputFormat.format(date);
            } else {
                return "";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
