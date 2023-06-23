package com.example.chitchat.fragment;

import android.content.Intent;
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
import com.example.chitchat.data.Chat.ChatDao;
import com.example.chitchat.data.Chat.ChatEntity;
import com.example.chitchat.data.Chat.ChatItemData;
import com.example.chitchat.data.Chat.ChatsDatabase;
import com.example.chitchat.data.User.UserDao;
import com.example.chitchat.data.User.UserDatabase;
import com.example.chitchat.data.User.UserEntity;

import java.util.ArrayList;
import java.util.List;

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

    public ChatFragment() {
    }

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
        List<ChatItemData> chatItems = createChatItems(chats);

        // Create and set the adapter
        adapter = new ChatAdapter(getContext(), chatItems);
        adapter.setOnItemClickListener(this); // Set the item click listener
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private List<ChatItemData> createChatItems(List<ChatEntity> all_chats) {
        List<ChatItemData> chatItems = new ArrayList<>();

        for (ChatEntity chat : all_chats) {
            for (UserEntity user : chat.getUsers()) {
                if (!user.getUsername().equals(currentUser_str)) {
                    UserEntity otherUser = user;
                    chatItems.add(
                            new ChatItemData(
                                    otherUser.getProfilePic(),
                                    otherUser.getDisplayName(),
                                    "", ""));
                }
            }
        }
        return chatItems;
    }

    // Implement the item click listener method
    //TODO: change to chat screen with the other user on click
    @Override
    public void onItemClick(ChatItemData chatItem) {
        System.out.println(chatItem.getDisplayName());
        Intent intent = new Intent(getContext(), ChatActivity.class);
        intent.putExtra("displayName",chatItem.getDisplayName());
        intent.putExtra("profilePic",chatItem.getProfilePic());
        //TODO if you want to pass more argument to the chat
        startActivity(intent);
    }

    // todo change
    public void refreshUserList() {
        new Thread(() -> {
            ChatsDatabase chatsDatabase = ChatsDatabase.getUserDatabase(getContext());
            ChatDao chatDao = chatsDatabase.chatDao();
            List<ChatEntity> chats = chatDao.getAllChats();
            requireActivity().runOnUiThread(() -> {
                List<ChatItemData> chatItems = createChatItems(chats);
                if (adapter != null) {
                    adapter.updateChatItems(chatItems);
                }
            });
        }).start();
    }
}
