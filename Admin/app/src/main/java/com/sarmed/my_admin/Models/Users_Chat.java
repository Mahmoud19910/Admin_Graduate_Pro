package com.sarmed.my_admin.Models;

public class Users_Chat {
    private String id;
    private String name;
    private String photoUrl;
    private String phone;
    private boolean isSession;

    public Users_Chat(String id, String name, String photoUrl, String phone, boolean isSession) {
        this.id = id;
        this.name = name;
        this.photoUrl = photoUrl;
        this.phone = phone;
        this.isSession = isSession;
    }

    public Users_Chat() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isSession() {
        return isSession;
    }

    public void setSession(boolean session) {
        isSession = session;
    }
}
