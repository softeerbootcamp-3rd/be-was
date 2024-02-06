package model;

import annotation.Column;

public class User {
    private Integer id;
    @Column
    private String userId;
    @Column
    private String password;
    @Column
    private String name;
    @Column
    private String email;

    public User() {
    }

    public User(Integer id, String userId, String password, String name, String email) {
        this.id = id;
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
}
