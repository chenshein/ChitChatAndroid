package com.example.chitchat.adapter;

import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chitchat.R;
import com.example.chitchat.data.Msg.Message;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Message> messageList;
    private String currentUsername;

    public MessageAdapter(List<Message> messageList, String currentUsername) {
        this.messageList = messageList;
        this.currentUsername = currentUsername;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);

        // Set the message content
        holder.contentTextView.setText(message.getContent());

        // Set the appropriate alignment for the message bubble based on the sender
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.bubbleLayout.getLayoutParams();
        if (message.getSender().equals(currentUsername)) {
            // Align to the right for the current user's messages
            params.gravity = Gravity.END;
            holder.bubbleLayout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.sender_shape));
        } else {
            // Align to the left for other users' messages
            params.gravity = Gravity.START;
            holder.bubbleLayout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.reciver_shape));
        }
        holder.bubbleLayout.setLayoutParams(params);
    }


    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        LinearLayout bubbleLayout;
        TextView contentTextView;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            bubbleLayout = itemView.findViewById(R.id.message_bubble_layout);
            contentTextView = itemView.findViewById(R.id.message_content_text);
        }
    }
}
