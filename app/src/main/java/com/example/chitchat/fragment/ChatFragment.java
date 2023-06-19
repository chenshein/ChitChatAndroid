package com.example.chitchat.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chitchat.R;
import com.example.chitchat.adapter.ChatAdapter;
import com.example.chitchat.data.Chat.ChatItemData;
import com.example.chitchat.data.User.UserDao;
import com.example.chitchat.data.User.UserDatabase;
import com.example.chitchat.data.User.UserEntity;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<UserEntity> friendList;
    private UserEntity current_user;
    private String currentUser = "";
    private ChatAdapter adapter;

    public ChatFragment(String current_username) {
        this.currentUser = current_username;
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

        // Start a new thread to fetch friendList and setup RecyclerView
        new Thread(() -> {
            UserDatabase userDatabase = UserDatabase.getUserDatabase(getContext());
            UserDao userDao = userDatabase.userDao();
            this.current_user = userDao.get(currentUser);
            List<UserEntity> contacts = current_user.getUserList();
            friendList = contacts;
            // Call setupRecyclerView() on the main thread
            requireActivity().runOnUiThread(this::setupRecyclerView);
        }).start();
    }

    private void setupRecyclerView() {
        List<ChatItemData> chatItems = createChatItems(friendList);

        // Create and set the adapter
        adapter = new ChatAdapter(getContext(), chatItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
    private List<ChatItemData> createChatItems(List<UserEntity> friendList) {

        List<ChatItemData> chatItems = new ArrayList<>();

        // Convert each UserEntity in the friendList to a ChatItemData
        for (UserEntity friend : friendList) {
            ChatItemData chatItem = new ChatItemData();
            chatItem.setProfilePic(friend.getProfilePic());
            chatItem.setDisplayName(friend.getDisplayName());
            // Set other properties as needed
            chatItems.add(chatItem);
        }

        return chatItems;
    }

}
