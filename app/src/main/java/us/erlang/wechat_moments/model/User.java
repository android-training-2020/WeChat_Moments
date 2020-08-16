package us.erlang.wechat_moments.model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("username")
    private String userName;
    private String nick;
    private String avatar;
    @SerializedName("profile-image")
    private String profileImage;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
