package com.example.tapme;

import android.widget.EditText;

public class Users {
    String profilepic,mail,userName,password,userId,lastMessage,status,fcmToken;

    public Users(String userId,  String userName,String mail, String password, String profilepic, String status) {
        this.profilepic = profilepic;
        this.mail = mail;
        this.userName = userName;
        this.password = password;
        this.userId = userId;
        this.status = status;
    }


    public Users(){

    }
    public String getFcmToken(){
        return fcmToken;
    }
    public void setFcmToken(String fcmToken){
        this.fcmToken = fcmToken;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
