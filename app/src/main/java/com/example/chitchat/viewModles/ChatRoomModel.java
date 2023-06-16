package com.example.chitchat.viewModles;

import java.sql.Timestamp;
import java.util.List;

public class ChatRoomModel {
    String chatroomId;
    List<String> users_name;
    Timestamp last_msg_time;

    public ChatRoomModel() {
    }

    public String getChatroomId() {
        return chatroomId;
    }

    public void setChatroomId(String chatroomId) {
        this.chatroomId = chatroomId;
    }

    public List<String> getUsers_name() {
        return users_name;
    }

    public void setUsers_name(List<String> users_name) {
        this.users_name = users_name;
    }

    public Timestamp getLast_msg_time() {
        return last_msg_time;
    }

    public void setLast_msg_time(Timestamp last_msg_time) {
        this.last_msg_time = last_msg_time;
    }

    public ChatRoomModel(String chatroomId, List<String> users_name, Timestamp last_msg_time) {
        this.chatroomId = chatroomId;
        this.users_name = users_name;
        this.last_msg_time = last_msg_time;
    }
}
