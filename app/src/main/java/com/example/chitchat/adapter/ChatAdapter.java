package com.example.chitchat.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chitchat.R;
import com.example.chitchat.data.Chat.ChatItemData;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private Context context;
    private List<ChatItemData> chatItems;

    public ChatAdapter(Context context, List<ChatItemData> chatItems) {
        this.context = context;
        this.chatItems = chatItems;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chats_user_row, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatItemData chatItem = chatItems.get(position);

        holder.displayName.setText(chatItem.getDisplayName());
        holder.lastMessage.setText(chatItem.getLastMessage());
        holder.created.setText(chatItem.getCreated());
//        String userImageUrl = chatItem.getProfilePic();
//        byte[] decodedImage = Base64.decode(userImageUrl, Base64.DEFAULT);
//        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length);
//        profilePic.setImageBitmap(bitmap);

        //todo pic
    }

    @Override
    public int getItemCount() {
        return chatItems.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profilePic;
        TextView displayName, lastMessage, created;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.profile_pic_chats);
            displayName = itemView.findViewById(R.id.chats_user_name);
            lastMessage = itemView.findViewById(R.id.last_message_text);
            created = itemView.findViewById(R.id.time_text);
        }
    }

    //todo change
    public void updateChatItems(List<ChatItemData> chatItems) {
        this.chatItems = chatItems;
        notifyDataSetChanged();
    }

}