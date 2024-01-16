package model;

import request.SignUpRequest;

public class User {
    private String userdId;
    private String password;
    private String name;
    private String email;

    public User(SignUpRequest signUpRequest) {
        this.userId = signUpRequest.getUserId();
        this.password = signUpRequest.getPassword();
        this.name = signUpRequest.getName();
        this.email = signUpRequest.getEmail();
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
