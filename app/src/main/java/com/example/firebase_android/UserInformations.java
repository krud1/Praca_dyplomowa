package com.example.firebase_android;

public class UserInformations {

    String nickname;
    String email;
    String phone;
    String key;
    String uid;

    public UserInformations(String nickname, String email, String phone, String uid){
        this.nickname = nickname;
        this.email = email;
        this.phone = phone;
        this.uid = uid;
    }

    public UserInformations(){
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUid() { return uid; }

    public void setUid(String uid) { this.uid = uid; }
}
