package com.example.luxlexicaapp.data.entities;

public class CommunityPost {
    private final String userName;
    private final String timeAgo;
    private final String content;
    private final int avatarResId;

    public CommunityPost(String userName, String timeAgo, String content, int avatarResId) {
        this.userName = userName;
        this.timeAgo = timeAgo;
        this.content = content;
        this.avatarResId = avatarResId;
    }

    public String getUserName() {
        return userName;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public String getContent() {
        return content;
    }

    public int getAvatarResId() {
        return avatarResId;
    }
}
