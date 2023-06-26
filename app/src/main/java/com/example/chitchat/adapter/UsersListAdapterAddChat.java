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

import com.example.chitchat.Activity.AllChatsActivity;
import com.example.chitchat.R;
import com.example.chitchat.api.ChatAPI;
import com.example.chitchat.data.Chat.ChatDao;
import com.example.chitchat.data.Chat.ChatEntity;
import com.example.chitchat.data.Chat.ChatsDatabase;
import com.example.chitchat.data.ChatCallback;
import com.example.chitchat.data.Msg.Message;
import com.example.chitchat.data.User.UserDao;
import com.example.chitchat.data.User.UserDatabase;
import com.example.chitchat.data.User.UserEntity;

import java.sql.Timestamp;
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
    private List<UserEntity> users = new ArrayList<>();
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
            holder.username.setText(curr.getDisplayName()); //set to show displayName
            //set wanted user's photo
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
    //when we click a user that we want to add to our chats
    private void onItemClick(View itemView, UserEntity user) {

        Context context = itemView.getContext();
        Intent intent = new Intent(context, AllChatsActivity.class);

        // Pass the current user back to see all his chats with the new adding chat
        intent.putExtra("username", curr_username);

        Runnable asyncRunnable = () -> {
            UserDatabase userDatabase = UserDatabase.getUserDatabase(context);
            UserDao userDao = userDatabase.userDao();
            //get current user Entity with password in order to use it for the token
            UserEntity.UserWithPws currentUser = userDao.get(curr_username);

            // in order to add a new chat
            if (currentUser != null) {
                //TODO in to be from db server to know if the chat exist
                ChatAPI chatAPI = new ChatAPI();
                chatAPI.addChat(currentUser, user.getUsername(), new ChatCallback() {
                    @Override
                    public void onSuccessRes(String returnVal) { // chat added !! returnVal is the id that the server return
                        if (!returnVal.equals("failed")) {
                            //create the chat in ROOM db
                            new Thread(() -> {
                                List<UserEntity> list = new ArrayList<>();
                                list.add((UserEntity) currentUser);
                                list.add(user);
                                List<Message> messageList = new ArrayList<>();

                                ChatEntity new_chat = new ChatEntity(returnVal, list, messageList, "", new Timestamp(System.currentTimeMillis()));
                                ChatsDatabase chatsDatabase = ChatsDatabase.getUserDatabase(context);
                                ChatDao chatDao = chatsDatabase.chatDao();
                                chatDao.createChat(new_chat);
                            }).start();
                        }
                    }

                    @Override
                    public void onSuccess(List<ChatEntity> chatEntities) {
                    }

                    @Override
                    public void onFailure(String errorMessage) {

                    }
                });


            }


        };
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        executor.execute(asyncRunnable);
    }


    // add the all users list to the
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

    //show on the search screen the wanted user to add to chats :)
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

//    public List<UserEntity> getUsers() {
//        return users;
//    }

    private boolean check_if_wanted_user_in_list(UserEntity user, List<ChatEntity> list) {
        for (ChatEntity chat : list) {
            System.out.println(chat.getChatIdServer());
            List<UserEntity> participants = chat.getUsers();
            if (participants != null && participants.contains(user)) {
                return true; // User found in the participants list
            }
        }
        return false; // User not found in any chat's participants list
    }


    private void add_chat_local_db(UserEntity currentUser, UserEntity user, Context context){


    }
}


