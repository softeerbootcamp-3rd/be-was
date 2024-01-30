package dto;

import annotation.NotEmpty;

public class UserCreateDto {
    @NotEmpty
    private String userId;
    @NotEmpty
    private String password;
    @NotEmpty
    private String name;
    @NotEmpty
    private String email;

    public UserCreateDto() {}

    public UserCreateDto(String userId, String password, String name, String email) {
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
}
