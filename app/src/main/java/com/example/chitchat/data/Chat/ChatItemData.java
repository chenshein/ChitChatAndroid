package com.example.chitchat.data.Chat;

public class ChatItemData {

    private String username;
    private String profilePic;
    private String displayName;
    private String lastMessage;
    private String created;


    public ChatItemData(String profilePic,String displayName,String lastMessage,String created){
        this.created=created;
        this.lastMessage = lastMessage;
        this.displayName = displayName;
        this.profilePic = profilePic;
    }

    public ChatItemData(String username, String profilePic,String displayName,String lastMessage,String created){
        this.username = username;
        this.created=created;
        this.lastMessage = lastMessage;
        this.displayName = displayName;
        this.profilePic = profilePic;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUsername() {
        return username;
    }
}
