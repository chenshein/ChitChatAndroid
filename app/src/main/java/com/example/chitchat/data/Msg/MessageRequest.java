package com.example.chitchat.data.Msg;

import com.google.gson.annotations.SerializedName;

public class MessageRequest {
    @SerializedName("msg")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageRequest(String message) {
        this.message = message;
    }

}
