package com.example.chitchat.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chitchat.Activity.ChatActivity;
import com.example.chitchat.Activity.LoginActivity;
import com.example.chitchat.Activity.SignUpActivity;
import com.example.chitchat.R;
import com.example.chitchat.data.User.UserEntity;

import java.util.ArrayList;
import java.util.List;

public class UsersListAdapterAddChat extends RecyclerView.Adapter<UsersListAdapterAddChat.UserViewHolder> {
    class UserViewHolder extends RecyclerView.ViewHolder{
        private final TextView username;
        private final ImageView user_pic;

        private UserViewHolder(View itemView){
            super(itemView);
            username = itemView.findViewById(R.id.user_name_text);
            user_pic = itemView.findViewById(R.id.profile_image_view);
        }
    }

    private final LayoutInflater layoutInflater;
    private List<UserEntity> users;
    private final String curr_username;

    public UsersListAdapterAddChat(Context context,String username) {
        layoutInflater = LayoutInflater.from(context);
        this.curr_username = username;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = layoutInflater.inflate(R.layout.search_user_recycler_row, parent,false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder,int position){
        if(users != null){
            final UserEntity curr = users.get(position);
            String s =curr.getUsername();
            holder.username.setText(curr.getUsername());
            //TODO get picture
//            holder.user_pic.setImageResource(curr.getProfilePic());
        }
        UserEntity user = users.get(position);

        // Navigate to chat page
        holder.itemView.setOnClickListener(v -> {
            onItemClick(holder.itemView,user);
        });
    }

    private void onItemClick(View itemView, UserEntity user) {
        Context context = itemView.getContext();
        Intent intent = new Intent(context, ChatActivity.class);
        passUserAsIntent(intent, user);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }




    public void setUsers(List<UserEntity> list){
        List<UserEntity> list_without_curr = new ArrayList<>();
        for (UserEntity user : list) {
            if (!curr_username.equals(user.getUsername())) {
                list_without_curr.add(user);
            }
        }
        users=list_without_curr;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(users!=null){
            return users.size();
        }
        return 0;
    }

    public List<UserEntity> getUsers() { return users;}


    public void passUserAsIntent(Intent intent,UserEntity user){
        intent.putExtra("username",user.getUsername());
        intent.putExtra("displayName",user.getDisplayName());
        intent.putExtra("profilePic",user.getProfilePic());
    }
}
