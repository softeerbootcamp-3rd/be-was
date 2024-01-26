package dto;

import model.User;

public class UserDto {
    private String userId;
    private String password;
    private String name;
    private String email;

    public UserDto() {
    }

    public UserDto(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public UserDto(User user){
        this.userId = user.getUserId();
        this.password = user.getPassword();
        this.name = user.getName();
        this.email = user.getEmail();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String toJsonString() {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"userId\":\"").append(userId).append("\",");
        json.append("\"password\":\"").append(password).append("\",");
        json.append("\"name\":\"").append(name).append("\",");
        json.append("\"email\":\"").append(email).append("\"");
        json.append("}");
        return json.toString();
    }
}
