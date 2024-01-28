package model;

import db.Database;

import java.util.HashMap;

public class User {

    private String userId;
    private String password;
    private String name;
    private String email;

    public User(UserInfo userInfo) {
        this.userId = userInfo.getInfo().get("userId");
        this.password = userInfo.getInfo().get("password");
        this.name = userInfo.getInfo().get("name");
        this.email = userInfo.getInfo().get("email");
    }
    public User(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }
    public String getUserId() {return this.userId;}
    public String getPassword() {return this.password;}
    public String getName() {return this.name;}
    public String getEmail() {return this.email;}
    @Override
    public String toString() {
        String userId = this.userId;
        String password = this.password;
        String name = this.name;
        String email = this.email;
        return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email  + "]";
    }

    public boolean verifyUser() {
        String userId = this.userId;
        String password = this.password;
        String name = this.name;
        String email = this.email;
        if(userId.isEmpty() || password.isEmpty()
                || name.isEmpty() || email.isEmpty()) return false;
        if(Database.findUserById(userId) != null) return false;
        return true;
    }
}