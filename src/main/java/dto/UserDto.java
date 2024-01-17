package dto;

import java.util.Map;

public class UserDto {
    private String userId;
    private String password;
    private String name;
    private String email;

    public UserDto(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public static UserDto buildUserDtoFromParams (Map<String, String> createUserParams) {
        String userId = createUserParams.get("userId");
        String password = createUserParams.get("password");
        String name = createUserParams.get("name");
        String email = createUserParams.get("email");

        if (userId != null && password != null && name != null && email != null) {
            return new UserDto(userId, password, name, email);
        } else {
            throw new IllegalArgumentException("Invalid Parameters");
        }
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
