package model;

import db.Database;

public class User {
    private String userId;
    private String password;
    private String name;
    private String email;

    public User(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";
    }

    public static String verifyUser(User user) {
        if(user.getUserId().isEmpty()
                || user.getEmail().isEmpty()
                || user.getName().isEmpty()
                || user.getPassword().isEmpty()) return "입력란에 공백이 존재하면 안됩니다.";
        else if(Database.findUserById(user.getUserId()) != null) return "중복되는 아이디 입니다.";
        return "성공";
    }
}
