package com.example.chitchat.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chitchat.Activity.ChatActivity;
import com.example.chitchat.R;
import com.example.chitchat.api.ChatAPI;
import com.example.chitchat.data.Chat.ChatDao;
import com.example.chitchat.data.Chat.ChatEntity;
import com.example.chitchat.data.Chat.ChatsDatabase;
import com.example.chitchat.data.User.UserDao;
import com.example.chitchat.data.User.UserDatabase;
import com.example.chitchat.data.User.UserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersListAdapterAddChat extends RecyclerView.Adapter<UsersListAdapterAddChat.UserViewHolder> {
    class UserViewHolder extends RecyclerView.ViewHolder {
        private final TextView username;
        private final CircleImageView user_pic;

        private UserViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.user_name_text);
            user_pic = itemView.findViewById(R.id.profile_image_view);
        }
    }

    private final LayoutInflater layoutInflater;
    private List<UserEntity.UserWithPws> users = new ArrayList<>();
    private final String curr_username;

    public UsersListAdapterAddChat(Context context, String username) {
        layoutInflater = LayoutInflater.from(context);
        this.curr_username = username;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.search_user_recycler_row, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        if (users != null) {
            final UserEntity.UserWithPws curr = users.get(position);
            holder.username.setText(curr.getUsername());
            String userImageUrl = curr.getProfilePic();
            byte[] decodedImage = Base64.decode(userImageUrl, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length);
            holder.user_pic.setImageBitmap(bitmap);
        }
        UserEntity.UserWithPws user = users.get(position);

        // Navigate to chat page
        holder.itemView.setOnClickListener(v -> {
            onItemClick(holder.itemView, user);
        });
    }

    private Executor executor = Executors.newSingleThreadExecutor();
    //when we click a user that we want to add to our chats
    private void onItemClick(View itemView, UserEntity.UserWithPws user) {
        Context context = itemView.getContext();
        Intent intent = new Intent(context, ChatActivity.class);

        // Pass the other user to the chat activity
        intent.putExtra("username", user.getUsername());
        intent.putExtra("profilePic", user.getProfilePic());
        intent.putExtra("displayName", user.getDisplayName());
        intent.putExtra("current_username", curr_username);

        Runnable asyncRunnable = () -> {
            UserDatabase userDatabase = UserDatabase.getUserDatabase(context);
            UserDao userDao = userDatabase.userDao();
            //get current user Entity
            UserEntity.UserWithPws currentUser = userDao.get(curr_username);
            UserEntity currentUserWithoutPws = new UserEntity(currentUser.getUsername(),currentUser.getDisplayName(),currentUser.getProfilePic());
            if (currentUser != null) {
                ChatsDatabase chatsDatabase = ChatsDatabase.getUserDatabase(context);
                ChatDao chatDao = chatsDatabase.chatDao();
                List<ChatEntity> all_chats = chatDao.getAllChats();
                UserEntity user_to_add = new UserEntity(user.getUsername(),user.getDisplayName(),user.getProfilePic());
                boolean is_there = check_if_wanted_user_in_list(user_to_add,all_chats);
                if(!is_there){
                    //make new chat
                    ChatEntity chat = new ChatEntity(currentUserWithoutPws,user_to_add);
                    System.out.println("new "+chat.getChatId());
                    // make chat in ROOM
                    chatDao.createChat(chat);
                    ChatAPI chatAPI = new ChatAPI();
                    // add to db in server
                    chatAPI.addChat(currentUser, user_to_add);
                }
//                // Add the user to the current user's friend list
//                currentUser.addUserToUserList(user);
//
//                // Update the user record in the database with the modified friend list
//                userDao.updateUser(currentUser);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        };
        executor.execute(asyncRunnable);
    }


    public void setUsers(List<String> list) {
        List<String> list_without_curr = new ArrayList<>();
        for (String user : list) {
            if (!curr_username.equals(user)) {
                list_without_curr.add(user);
                new Thread(()-> {
                    UserDatabase userDatabase = UserDatabase.getUserDatabase(layoutInflater.getContext());
                    UserDao userDao = userDatabase.userDao();
                    UserEntity.UserWithPws friend = userDao.get(user);
                    users.add(friend);
                }).start();
            }
        }
        notifyDataSetChanged();
    }

    public void setUser(UserEntity.UserWithPws user) {
        List<UserEntity.UserWithPws> list_without_curr = new ArrayList<>();
        if (!curr_username.equals(user.getUsername())) {
            list_without_curr.add(user);
        }
        users = list_without_curr;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        if (users != null) {
            return users.size();
        }
        return 0;
    }

    public List<UserEntity.UserWithPws> getUsers() {
        return users;
    }


    private boolean check_if_wanted_user_in_list(UserEntity user, List<ChatEntity> list) {
        for (ChatEntity chat : list) {
            System.out.println(chat.getChatId());
            List<UserEntity> participants = chat.getUsers();
            if (participants != null && participants.contains(user)) {
                return true; // User found in the participants list
            }
        }
        return false; // User not found in any chat's participants list
    }

}
