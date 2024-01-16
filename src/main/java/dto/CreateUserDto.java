package dto;

import model.User;

import java.util.Map;

public class CreateUserDto {

    private final String userId;
    private final String password;
    private final String name;
    private final String email;

    public CreateUserDto(Map<String, String> paramMap) {
        this.userId = paramMap.get("userId");
        this.password = paramMap.get("password");
        this.name = paramMap.get("name");
        this.email = paramMap.get("email");
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
}
