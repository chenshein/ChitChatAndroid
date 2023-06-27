package com.example.chitchat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chitchat.R;
import com.example.chitchat.data.Msg.Message;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ChatActivityViewHolder> {
    private final LayoutInflater mInflater;
    private List<Message> messages;
    private String currUsername;

    public MessageAdapter(Context context, List<Message> messages, String currUsername) {
        this.mInflater = LayoutInflater.from(context);
        this.messages = messages;
        this.currUsername = currUsername;
    }

    @NonNull
    @Override
    public ChatActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == 0) {
            itemView = mInflater.inflate(R.layout.sender_bubble, parent, false);
        } else {
            itemView = mInflater.inflate(R.layout.reciver_bubble, parent, false);
        }
        return new ChatActivityViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatActivityViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.bindMessage(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (message.getSender().getUsername().equals(currUsername)) {
            return 0; // Outgoing message
        } else {
            return 1; // Incoming message
        }
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    class ChatActivityViewHolder extends RecyclerView.ViewHolder {
        private TextView messageText;
        private TextView messageTime;

        ChatActivityViewHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.msgsendertyp);
//            messageTime = itemView.findViewById(R.id.messageTime);
        }

        void bindMessage(Message message) {
            messageText.setText(message.getContent());
//            String time = formatDateTime(message.getCreated());
//            messageTime.setText(time);
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
}