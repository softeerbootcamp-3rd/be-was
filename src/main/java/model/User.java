package model;

import annotation.Column;

public class User {
    @Column
    private String userId;
    @Column
    private String password;
    @Column
    private String name;
    @Column
    private String email;

    private User() {
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
}
