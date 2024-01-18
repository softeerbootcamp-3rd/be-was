package model;

import db.Database;

public class User {

    private UserInfo userInfo;

    public User(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
    public UserInfo getUserInfo() {return this.userInfo;}

    @Override
    public String toString() {
        String userId = userInfo.getInfo().get("userId");
        String password = userInfo.getInfo().get("password");
        String name = userInfo.getInfo().get("name");
        String email = userInfo.getInfo().get("email");
        return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";
    }

    public boolean verifyUser() {
        String userId = userInfo.getInfo().get("userId");
        String password = userInfo.getInfo().get("password");
        String name = userInfo.getInfo().get("name");
        String email = userInfo.getInfo().get("email");
        if(userId.isEmpty() || password.isEmpty()
                || name.isEmpty() || email.isEmpty()) return false;
        if(Database.findUserById(userId) != null) return false;
        return true;
    }
}