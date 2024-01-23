package model;

import db.Database;

import java.util.HashMap;

public class User {

    private UserInfo userInfo;

    public User(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
    public UserInfo getUserInfo() {return this.userInfo;}
    public String getUserId() {return this.userInfo.getInfo().get("userId");}
    public String getPassword() {return this.userInfo.getInfo().get("password");}
    public String getName() {return this.userInfo.getInfo().get("name");}
    public String getEmail() {return this.userInfo.getInfo().get("email");}
    @Override
    public String toString() {
        String userId = this.userInfo.getInfo().get("userId");
        String password = this.userInfo.getInfo().get("password");
        String name = this.userInfo.getInfo().get("name");
        String email = this.userInfo.getInfo().get("email");
        String sessionId = this.userInfo.getInfo().get("sessionId");
        return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + ", sessionId=" + sessionId + "]";
    }

    public boolean verifyUser() {
        String userId = this.userInfo.getInfo().get("userId");
        String password = this.userInfo.getInfo().get("password");
        String name = this.userInfo.getInfo().get("name");
        String email = this.userInfo.getInfo().get("email");
        if(userId.isEmpty() || password.isEmpty()
                || name.isEmpty() || email.isEmpty()) return false;
        if(Database.findUserById(userId) != null) return false;
        return true;
    }
}