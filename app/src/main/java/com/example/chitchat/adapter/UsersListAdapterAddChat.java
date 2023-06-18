package com.example.chitchat.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chitchat.Activity.ChatActivity;
import com.example.chitchat.R;
import com.example.chitchat.api.UserAPI;
import com.example.chitchat.data.User.UserDao;
import com.example.chitchat.data.User.UserDatabase;
import com.example.chitchat.data.User.UserEntity;
import com.squareup.picasso.Picasso;

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
    private List<UserEntity> users;
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
            final UserEntity curr = users.get(position);
            holder.username.setText(curr.getUsername());
            String userImageUrl = curr.getProfilePic();
            byte[] decodedImage = Base64.decode(userImageUrl, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length);
            holder.user_pic.setImageBitmap(bitmap);
        }
        UserEntity user = users.get(position);

        // Navigate to chat page
        holder.itemView.setOnClickListener(v -> {
            onItemClick(holder.itemView, user);
        });
    }

    private Executor executor = Executors.newSingleThreadExecutor();
    private void onItemClick(View itemView, UserEntity user) {
        Context context = itemView.getContext();
        Intent intent = new Intent(context, ChatActivity.class);

        // Pass the other user
        intent.putExtra("username", user.getUsername());
        intent.putExtra("profilePic", user.getProfilePic());
        intent.putExtra("displayName", user.getDisplayName());

        Runnable asyncRunnable = () -> {
            UserDatabase userDatabase = UserDatabase.getUserDatabase(context);
            UserDao userDao = userDatabase.userDao();
            UserEntity currentUser = userDao.get(curr_username);

            if (currentUser != null && !currentUser.getUserList().contains(user)) {
                // Add the user to the current user's friend list
                currentUser.addUserToUserList(user);

                // Update the user record in the database with the modified friend list
                userDao.updateUser(currentUser);

                UserAPI userAPI = new UserAPI();
                userAPI.addChat(currentUser, user);

                Log.d("UserDatabase", "Friend added: " + user.getUsername());
            }

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        };

        executor.execute(asyncRunnable);
    }


    public void setUsers(List<UserEntity> list) {
        List<UserEntity> list_without_curr = new ArrayList<>();
        for (UserEntity user : list) {
            if (!curr_username.equals(user.getUsername())) {
                list_without_curr.add(user);
            }
        }
        users = list_without_curr;
        notifyDataSetChanged();
    }

    public void setUser(UserEntity user) {
        List<UserEntity> list_without_curr = new ArrayList<>();
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

    public List<UserEntity> getUsers() {
        return users;
    }

}
