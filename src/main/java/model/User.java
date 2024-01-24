package model;

import dto.UserDto;

public class User {
    private String userId;
    private String password;
    private String name;
    private String email;

    public User() {
    }

    public User(UserDto userDto){
        this.name = userDto.getName();
        this.userId = userDto.getUserId();
        this.email = userDto.getEmail();
        this.password = userDto.getPassword();
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

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return new StringBuilder("User [userId=").append(userId).append(", password=").append(password).append(", name=").append(name).append(", email=").append(email).append("]").toString();
    }
}
